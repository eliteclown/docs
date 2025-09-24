package com.cybercity.application.repositories;

import com.cybercity.application.entities.WebinarEnrollmentEntity;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.WebinarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebinarEnrollmentRepository extends JpaRepository<WebinarEnrollmentEntity, Long> {

    // Find by user + webinar (to prevent duplicate enrollment)
    Optional<WebinarEnrollmentEntity> findByUserEntityAndWebinarEntity(UserEntity user, WebinarEntity webinar);
    
  
        List<WebinarEnrollmentEntity> findByUserEntity_UserId(Long userId);
        List<WebinarEnrollmentEntity> findByWebinarEntity_WebinarId(Long webinarId);
   


    // List all webinars a user enrolled in
    List<WebinarEnrollmentEntity> findByUserEntity(UserEntity user);

    // List all users enrolled in a webinar
    List<WebinarEnrollmentEntity> findByWebinarEntity(WebinarEntity webinar);
    
    boolean existsByUserEntity_UserIdAndWebinarEntity_WebinarId(Long userId, Long webinarId);
}
