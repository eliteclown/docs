package com.cybercity.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cybercity.application.advices.ApiResponse;
import com.cybercity.application.dtos.ChangePasswordRequest;
import com.cybercity.application.dtos.UserDTO;
import com.cybercity.application.services.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")


public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UserDTO createUser(@RequestBody UserDTO inputDTO){
        return userService.createNewUser(inputDTO);
    }

    @GetMapping("/get/{userId}")
    public UserDTO getUser(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/update/{userId}")
    public UserDTO updateUser(@RequestBody UserDTO updateDTO, @PathVariable Long userId){
        return userService.updateUserById(updateDTO,userId);
    }
    
    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        boolean verified = userService.verifyEmail(token);
        return verified ? ResponseEntity.ok("Email verified successfully. You can now log in.")
                        : ResponseEntity.badRequest().body("Invalid or expired token");
    }
    
    @GetMapping("/verifyagain")
    public ResponseEntity<String> verifyAccountAgain(@RequestParam("email") String email) {
        String message=userService.verifyEmailAgain(email);
        return  ResponseEntity.ok(message);
                      
    }
    
    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        String message = userService.forgotPassword(email);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest request) {
        String result = userService.changePassword(request);
        return new ApiResponse<>(result);
    }
    
    @GetMapping("/check-email")
    public ApiResponse<String> checkEmail(
            @RequestParam String email,
            @RequestParam Long courseId) {

        String status = userService.checkUserForPayment(email, courseId);

        switch (status) {
            case "NOT_REGISTERED":
                return new ApiResponse<>("User not registered");

            case "NOT_VERIFIED":
                return new ApiResponse<>("Email not verified yet");

            case "USER_ALREADY_ENROLLED":
                return new ApiResponse<>("User already enrolled in this course");

            default:
                return new ApiResponse<>("VERIFIED");
        }
    }
    
    @GetMapping("/check-emailforWebinar")
    public ApiResponse<String> checkEmailForWebinar(
            @RequestParam String email,
            @RequestParam Long webinarId) {

        String status = userService.checkUserForWebinarEnrollment(email, webinarId);

        switch (status) {
            case "NOT_REGISTERED":
                return new ApiResponse<>("User not registered");

            case "NOT_VERIFIED":
                return new ApiResponse<>("Email not verified yet");

            case "USER_ALREADY_ENROLLED":
                return new ApiResponse<>("User already enrolled in this webinar");

            default:
                return new ApiResponse<>("VERIFIED");
        }
    }

    
    @GetMapping("/id-by-email")
    public ResponseEntity<Long> getUserId(@RequestParam String email) {
        Long userId = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }

}
