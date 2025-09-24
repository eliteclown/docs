package com.cybercity.application.controllers;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cybercity.application.advices.ApiResponse;
import com.cybercity.application.entities.PaymentEntity;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.enums.PaymentStatus;
import com.cybercity.application.repositories.PaymentRepository;
import com.cybercity.application.repositories.UserRepository;
import com.cybercity.application.services.RazorpayService;
import com.razorpay.Order;
import com.razorpay.Payment;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
	 private final RazorpayService razorpayService;
	    private final PaymentRepository paymentRepository;
	  private final UserRepository userRepo;
	  
	  

	    public PaymentController(RazorpayService razorpayService, PaymentRepository paymentRepository,UserRepository userRepo) {
	        this.razorpayService = razorpayService;
	        this.paymentRepository = paymentRepository;
	        this.userRepo =userRepo;
	    }
	    
	    
	    @PostMapping("/create-order")
	    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> requestData) {
	    	
	    	
	    	 try {
	             BigDecimal amount = new BigDecimal(requestData.get("amount").toString());
	             String email = requestData.get("email").toString();

	             Map<String, Object> orderDetails = razorpayService.creatingAndSavingOrder(amount, email);
	             return ResponseEntity.ok(orderDetails);

	         } catch (Exception e) {
	             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	         }
	    	 
	    	 
	    	 
     
	    }
	    
	    
	    @PostMapping("/verify-payment")
	    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> data) {
	        try {
	            String razorpayPaymentId = data.get("razorpay_payment_id").toString();
	            String razorpayOrderId = data.get("razorpay_order_id").toString();
	            String razorpaySignature = data.get("razorpay_signature").toString();

	            // 1️ Verify signature
	            if (!razorpayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature)) {
	                throw new RuntimeException("Invalid payment signature!");
	            }

	            // 2 Fetch payment status
	            Payment razorpayPayment = razorpayService.getPayment(razorpayPaymentId);
	            String status = razorpayPayment.get("status");

	            // 3️ Update DB
	            PaymentEntity paymentEntity = paymentRepository.findByOrderId(razorpayOrderId)
	                    .orElseThrow(() -> new RuntimeException("Payment not found for orderId: " + razorpayOrderId));

	            paymentEntity.setStatus(EnumSet.noneOf(PaymentStatus.class));
	            if ("captured".equals(status)) paymentEntity.setStatus(EnumSet.of(PaymentStatus.SUCCESS));
	            else if ("failed".equals(status)) paymentEntity.setStatus(EnumSet.of(PaymentStatus.FAILURE));
	            else paymentEntity.setStatus(EnumSet.of(PaymentStatus.CREATED));

	            paymentRepository.save(paymentEntity);

	            return ResponseEntity.ok(Map.of(
	                    "status", status,
	                    "orderId", razorpayOrderId,
	                    "paymentId", razorpayPaymentId
	            ));
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	        }
	    }
	    
	    
	    @PutMapping("/update-status")
	    public ApiResponse<String> updateStatus(@RequestBody Map<String, String> request) {
	        String orderId = request.get("orderId");
	        PaymentStatus status = PaymentStatus.valueOf(request.get("status")); // CREATED / SUCCESS / FAILURE

	        String response = razorpayService.updatePaymentStatus(orderId, status);
	        return  new ApiResponse<>(response);
	    }
   
}
