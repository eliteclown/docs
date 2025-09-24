package com.cybercity.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cybercity.application.dtos.WebinarEnrollmentDTO;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.WebinarEnrollmentEntity;
import com.cybercity.application.entities.WebinarEntity;
import com.cybercity.application.repositories.UserRepository;
import com.cybercity.application.repositories.WebinarEnrollmentRepository;
import com.cybercity.application.repositories.WebinarRepository;

@Service
public class WebinarEnrollmentService {

    private final WebinarEnrollmentRepository enrollmentRepo;
    private final UserRepository userRepo;
    private final WebinarRepository webinarRepo;
    private final ModelMapper modelMapper;

    public WebinarEnrollmentService(WebinarEnrollmentRepository enrollmentRepo,
                                    UserRepository userRepo,
                                    WebinarRepository webinarRepo,
                                    ModelMapper modelMapper) {
        this.enrollmentRepo = enrollmentRepo;
        this.userRepo = userRepo;
        this.webinarRepo = webinarRepo;
        this.modelMapper = modelMapper;
    }

    // ✅Enroll user into webinar using email + webinarId
    public WebinarEnrollmentDTO enrollUser(String email, Long webinarId) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        WebinarEntity webinar = webinarRepo.findById(webinarId)
                .orElseThrow(() -> new RuntimeException("Webinar not found with id: " + webinarId));

        //  Check if already enrolled
        boolean alreadyEnrolled = enrollmentRepo.findByUserEntity_UserId(user.getUserId()).stream()
                .anyMatch(enrollment -> enrollment.getWebinarEntity().getWebinarId().equals(webinarId));

        if (alreadyEnrolled) {
            throw new RuntimeException("User is already enrolled in this webinar!");
        }

        WebinarEnrollmentEntity enrollment = WebinarEnrollmentEntity.builder()
                .userEntity(user)
                .webinarEntity(webinar)
                .build();

        WebinarEnrollmentEntity saved = enrollmentRepo.save(enrollment);
        return modelMapper.map(saved, WebinarEnrollmentDTO.class);
    }

    // Get enrollment by id
    public WebinarEnrollmentDTO getEnrollmentById(Long enrollmentId) {
        WebinarEnrollmentEntity enrollment = enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
        return modelMapper.map(enrollment, WebinarEnrollmentDTO.class);
    }

    // Get enrollments of a user
    public List<WebinarEnrollmentDTO> getEnrollmentsByUser(Long userId) {
        return enrollmentRepo.findByUserEntity_UserId(userId).stream()
                .map(e -> modelMapper.map(e, WebinarEnrollmentDTO.class))
                .collect(Collectors.toList());
    }

    // Get enrollments of a webinar
    public List<WebinarEnrollmentDTO> getEnrollmentsByWebinar(Long webinarId) {
        return enrollmentRepo.findByWebinarEntity_WebinarId(webinarId).stream()
                .map(e -> modelMapper.map(e, WebinarEnrollmentDTO.class))
                .collect(Collectors.toList());
    }

    // Cancel enrollment
    public void cancelEnrollment(Long enrollmentId) {
        WebinarEnrollmentEntity enrollment = enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
        enrollmentRepo.delete(enrollment);
    }
    
    @Transactional(readOnly = true)
    public boolean isUserEnrolledInWebinar(Long userId, Long webinarId) {
        return enrollmentRepo.existsByUserEntity_UserIdAndWebinarEntity_WebinarId(userId, webinarId);
    }
}
