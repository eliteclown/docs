package com.cybercity.application.services;

import static com.cybercity.application.entities.enums.CompletionStatus.ONGOING;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cybercity.application.dtos.EnrollmentDTO;
import com.cybercity.application.entities.CourseEntity;
import com.cybercity.application.entities.EnrollmentEntity;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.enums.CompletionStatus;
import com.cybercity.application.exceptions.CourseNotFoundException;
import com.cybercity.application.exceptions.UserNotFoundException;
import com.cybercity.application.repositories.CourseRepository;
import com.cybercity.application.repositories.EnrollmentRepository;
import com.cybercity.application.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public EnrollmentDTO enrollNewUser(String email ,Long courseId,String category ){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));
        CourseEntity courseEntity = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException("Course not found"));
        EnrollmentEntity enrollmentEntity = new EnrollmentEntity();

        enrollmentEntity.setUserEntity(userEntity);
        enrollmentEntity.setCourseEntity(courseEntity);
        LocalDateTime now = LocalDateTime.now();
        enrollmentEntity.setEnrolledAt(now);
        enrollmentEntity.setExpiredAt(now.plusYears(1));

        Set<CompletionStatus> status= new HashSet<>();
        status = Set.of(ONGOING);

        enrollmentEntity.setCategory(category);
        enrollmentEntity.setStatus(status);

        EnrollmentEntity savedEntity = enrollmentRepository.save(enrollmentEntity);

        return modelMapper.map(savedEntity,EnrollmentDTO.class);
    }


    public List<EnrollmentDTO> courseEnrolledByUserId(Long userId){
        UserEntity userEntity =userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        List<EnrollmentEntity> enrollmentEntities = enrollmentRepository.findByUserEntity(userEntity);

        List<EnrollmentDTO> enrollmentDTOS = enrollmentEntities
                .stream()
                .map(enrollmentEntity -> modelMapper.map(enrollmentEntity,EnrollmentDTO.class))
                .collect(Collectors.toList());

        return enrollmentDTOS;
    }
    
    @Transactional(readOnly = true)
    public boolean isUserEnrolledInCourse(Long userId, Long courseId) {
        return enrollmentRepository.existsByUserEntity_UserIdAndCourseEntity_CourseId(userId, courseId);
    }
}
