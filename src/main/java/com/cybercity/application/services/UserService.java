package com.cybercity.application.services;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cybercity.application.dtos.ChangePasswordRequest;
import com.cybercity.application.dtos.UserDTO;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.VerificationToken;
import com.cybercity.application.exceptions.RegistrationFailException;
import com.cybercity.application.exceptions.UserNotFoundException;
import com.cybercity.application.repositories.UserRepository;
import com.cybercity.application.repositories.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MailService emailService;
    private final VerificationTokenRepository tokenRepository;
    private final EnrollmentService enrollmentService;
    private final WebinarEnrollmentService webinarEnrollmentService;
    
    @Autowired
    private JavaMailSender mailSender;

    public UserDTO createNewUser(UserDTO inputDTO){
    	 Optional<UserEntity> existingUserOpt = userRepository.findByEmail(inputDTO.getEmail());

    	    if (existingUserOpt.isPresent()) {
    	        UserEntity existingUser = existingUserOpt.get();

    	        if (!existingUser.isEnabled()) {
    	         
    	            throw new  RegistrationFailException("Email already exists but not verified. Please verify your email.");
    	        } else {
    	        	throw new  RegistrationFailException("Email already exists...");
    	           
    	        }
    	    }
    	 
    	    UserEntity userEntity = modelMapper.map(inputDTO, UserEntity.class);
    	    userEntity.setEnabled(false); // not active yet
         UserEntity savedUser = userRepository.save(userEntity);

         // generate verification token
         String token = UUID.randomUUID().toString();
         VerificationToken verificationToken = VerificationToken.builder()
                 .token(token)
                 .userEntity(savedUser)
                 .expiryDate(LocalDateTime.now().plusHours(24))
                 .build();
         tokenRepository.save(verificationToken);

         // send verification email
         emailService.sendVerificationEmail(savedUser, token);

         return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserDTO getUserById(Long userId){
        UserEntity userEntity=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
        return modelMapper.map(userEntity,UserDTO.class);
    }

    public UserDTO updateUserById(UserDTO updateDTO , Long userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
        UserEntity userEntity= modelMapper.map(updateDTO,UserEntity.class);
        userEntity.setUserId(userId);
        userEntity.setCreateDate(user.getCreateDate());
        userEntity.setUpdateDate(LocalDateTime.now());

        UserEntity savedEntity = userRepository.save(userEntity);
        return modelMapper.map(savedEntity, UserDTO.class);
    }
    
    public boolean verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        UserEntity user = verificationToken.getUserEntity();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken); // optional cleanup
        return true;
    }
    
    public String verifyEmailAgain(String email) {
    	
    	 Optional<UserEntity> existingUserOpt = userRepository.findByEmail(email);
    	 if (existingUserOpt.isEmpty()) {
    	        throw new RuntimeException("No user found with email: " + email);
    	       
    	    }
    	
    	 UserEntity userEntity = existingUserOpt.get();
    	 
    	 if(userEntity.isEnabled()) return "Email is already Verified";

 	    
    	  Optional<VerificationToken> existingTokenOpt = tokenRepository.findByUserEntity(userEntity);
    	    existingTokenOpt.ifPresent(tokenRepository::delete);


 	  

      // generate verification token
      String token = UUID.randomUUID().toString();
      VerificationToken verificationToken = VerificationToken.builder()
              .token(token)
              .userEntity(userEntity)
              .expiryDate(LocalDateTime.now().plusHours(24))
              .build();
      tokenRepository.save(verificationToken);

      // send verification email
      emailService.sendVerificationEmail(userEntity, token);
      
      return "Verification email send successfully";
    	
      
    }
    
    //Forgot password
    public String forgotPassword(String email) {
        Optional<UserEntity> existingUserOpt = userRepository.findByEmail(email);

        if (existingUserOpt.isEmpty()) {
            throw new UserNotFoundException("No user found with email: " + email);
        }

        UserEntity userEntity = existingUserOpt.get();

        // generate system-generated random password
        String newPassword = UUID.randomUUID().toString().substring(0, 8); // 8 chars

        // update user password in DB
        userEntity.setPassword(newPassword); 
        userRepository.save(userEntity);

        // send new password via email
        String subject = "Your New Password - Cyberdiction Technology";
        String body = "Hello " + userEntity.getUserName() + ",\n\n"
                + "Your password has been reset. Use the below password to login:\n\n"
                + newPassword + "\n\n"
                + "Please change your password after logging in for better security.\n\n"
                + "Regards,\nCyberdiction Team";
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEntity.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

       

        return "A new password has been sent to your email.";
    }
    
    //Change Password
    public String changePassword(ChangePasswordRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("No user found with email: " + request.getEmail());
        }

        UserEntity user = userOpt.get();

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new RuntimeException("Old password does not match");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return "Password changed successfully";
    }
    
    //Check Details of Student By Email and courseId for courses
    public String checkUserForPayment(String email,Long courseId) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "NOT_REGISTERED";
        }

        UserEntity user = userOpt.get();
        if (!user.isEnabled()) {
            return "NOT_VERIFIED";
        }
        
        boolean enrolled = enrollmentService.isUserEnrolledInCourse(user.getUserId(), courseId);
        if (enrolled) {
            return "USER_ALREADY_ENROLLED";
        } else {
        	return "VERIFIED";
        }
        
    }
    
  //Check Details of Student By Email and webinarId for webinars
    public String checkUserForWebinarEnrollment(String email,Long webinarId) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "NOT_REGISTERED";
        }

        UserEntity user = userOpt.get();
        if (!user.isEnabled()) {
            return "NOT_VERIFIED";
        }
        
        boolean enrolled =  webinarEnrollmentService.isUserEnrolledInWebinar(user.getUserId(), webinarId);
        if (enrolled) {
            return "USER_ALREADY_ENROLLED";
        } else {
        	return "VERIFIED";
        }
        
    }
    
    
    //Get user id by email
    public Long getUserIdByEmail(String email) {
    	 Optional<UserEntity> userOpt = userRepository.findByEmail(email);
    	 
    	 if (userOpt.isEmpty()) {
    		 throw new UserNotFoundException("User not found with email: " + email);

         }
    	 UserEntity user = userOpt.get();
    	 return user.getUserId();
    	 

    }

    public UserEntity getUserByIdAuth(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}


