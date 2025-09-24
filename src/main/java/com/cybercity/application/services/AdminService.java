package com.cybercity.application.services;

import com.cybercity.application.dtos.CourseDTO;
import com.cybercity.application.dtos.TeamsSessionDTO;
import com.cybercity.application.entities.CourseEntity;
import com.cybercity.application.entities.EnrollmentEntity;
import com.cybercity.application.entities.TeamsSessionEntity;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.exceptions.CourseNotFoundException;
import com.cybercity.application.exceptions.UserNotFoundException;
import com.cybercity.application.repositories.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
  
    private final EnrollmentRepository enrollmentRepository;
    private final TeamsSessionRepository teamsSessionRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    public CourseDTO createNewCourse(CourseDTO inputDTO,Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        CourseEntity courseEntity = modelMapper.map(inputDTO,CourseEntity.class);
        courseEntity.setUserEntity(userEntity);
        courseEntity.setInstructorName(userEntity.getUserName());
        CourseEntity savedEntity = courseRepository.save(courseEntity);
        return modelMapper.map(savedEntity,CourseDTO.class);
    }

    public TeamsSessionDTO createNewSession(TeamsSessionDTO inputDTO,Long courseId) throws MessagingException {
        CourseEntity courseEntity =courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException("Course not found"));
        TeamsSessionEntity teamsSessionEntity = modelMapper.map(inputDTO,TeamsSessionEntity.class);
        teamsSessionEntity.setCourseEntity(courseEntity);

        
        TeamsSessionEntity savedEntity = teamsSessionRepository.save(teamsSessionEntity);

        List<EnrollmentEntity> enrollmentEntities = enrollmentRepository.findByCourseEntity(courseEntity);

        for(EnrollmentEntity enrollmentEntity : enrollmentEntities){
            UserEntity userEntity = enrollmentEntity.getUserEntity();
            mailService.sendTeamsMeetingInvite(userEntity.getEmail(),userEntity.getUserName(),"Live Session Notification",teamsSessionEntity.getTeamsId(), String.valueOf(teamsSessionEntity.getStartDate()),"11:00 AM",teamsSessionEntity.getJoinUrl());
        }



        return modelMapper.map(savedEntity,TeamsSessionDTO.class);
    }
    
    

}
