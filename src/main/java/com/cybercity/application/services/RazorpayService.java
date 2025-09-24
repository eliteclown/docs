package com.cybercity.application.services;



import java.math.BigDecimal;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cybercity.application.entities.PaymentEntity;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.enums.PaymentStatus;
import com.cybercity.application.repositories.PaymentRepository;
import com.cybercity.application.repositories.UserRepository;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;



@Service
public class RazorpayService {
   
	 private final PaymentRepository paymentRepository;
	    private final ModelMapper modelMapper;
	    private final RazorpayClient razorpayClient;
	    private final String currency;
	    private final String keySecret;
	 
	  @Autowired
	  private UserRepository userRepo;
	

	   
	    public RazorpayService(
	            PaymentRepository paymentRepository,
	            ModelMapper modelMapper,
	            @Value("${razorpay.key_id}") String keyId,
	            @Value("${razorpay.key_secret}") String keySecret
	    ) throws RazorpayException {
	        this.paymentRepository = paymentRepository;
	        this.modelMapper = modelMapper;
	        this.razorpayClient = new RazorpayClient(keyId, keySecret);
	        this.currency = "INR"; // can make configurable if needed
	        this.keySecret = keySecret;
	     
	        
	    }

    public Order createOrder(BigDecimal amount) {
        validateAmount(amount);

        try {
            JSONObject options = new JSONObject();
            options.put("amount", amount.multiply(BigDecimal.valueOf(100))); // convert to paise
            options.put("currency", "INR");

            // Unique receipt ID for idempotency
            String uniqueReceipt = "order_" + Instant.now().toEpochMilli();
            options.put("receipt", uniqueReceipt);

            options.put("payment_capture", 1); // auto capture

            Order order = razorpayClient.orders.create(options);
            System.err.println("Created Razorpay order: {} for receipt: {}"+ order.get("id")+"is "+uniqueReceipt);

            return order;

        } catch (RazorpayException e) {
        	System.err.println("Failed to create Razorpay order: {}"+ e.getMessage());
           
            throw new RuntimeException("Unable to create order, please try again later.");
        }
    }

    public Map<String, Object> creatingAndSavingOrder(BigDecimal amount, String email) {
        try {
            // 1. Create order with Razorpay
            Order order = createOrder(amount);

            Optional<UserEntity> existingUser=userRepo.findByEmail(email);

            if (existingUser.isPresent()) {
                UserEntity user = existingUser.get();

                PaymentEntity payment = PaymentEntity.builder()
                        .userEntity(user)
                        .orderId(order.get("id"))
                        .amount(amount)
                        .method("RAZORPAY")
                        .status(EnumSet.of(PaymentStatus.CREATED))
                        .build();

                paymentRepository.save(payment);
            } else {
                
                throw new RuntimeException("User not found with email: " + email);
            }


         
            return Map.of(
                    "orderId", order.get("id"),
                    "amount", order.get("amount"),
                    "currency", order.get("currency"),
                    "receipt", order.get("receipt")
            );

        } catch (Exception e) {
        	 throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }
    
    
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid order amount");
        }
    }

    
    // Fetch payment from Razorpay
    public Payment getPayment(String paymentId) throws RazorpayException {
        return razorpayClient.payments.fetch(paymentId);
    }

    // Verify payment signature
    public boolean verifySignature(String orderId, String paymentId, String razorpaySignature) {
        try {
            String data = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(keySecret.getBytes(), "HmacSHA256"));
            byte[] digest = mac.doFinal(data.getBytes());

            // Convert digest to hex
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));

            return sb.toString().equals(razorpaySignature);
        } catch (Exception e) {
            return false;
        }
    }

   

    
    @Transactional
    public String updatePaymentStatus(String orderId, PaymentStatus status) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        payment.setStatus(EnumSet.of(status));
        paymentRepository.save(payment);

        return "Payment status updated to " + status;
    }

}
