//package com.cybercity.application.configs;
//
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class PaymentConfig {
//    @Value("${razorpay.key}")
//    private String key;
//    @Value("${razorpay.secret}")
//    private String secret;
//    @Bean
//    public RazorpayClient getRazorpayClient(){
//        try {
//            return new RazorpayClient(key, secret);
//        } catch (RazorpayException e) {
//            System.out.println("Unable to create client for razorpay");
//            throw new RuntimeException("Failed to instantiate razorpay");
//        }
//    }
//}
