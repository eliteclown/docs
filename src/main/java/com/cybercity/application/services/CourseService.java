package com.cybercity.application.services;

import com.cybercity.application.dtos.CourseDTO;
import com.cybercity.application.entities.CourseEntity;
import com.cybercity.application.exceptions.CourseNotFoundException;
import com.cybercity.application.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public List<CourseDTO> getAllCourses(){
        List<CourseEntity> courseEntities =courseRepository.findAll();

        List<CourseDTO> courseDTOS = courseEntities.stream()
                .map(courseEntity -> modelMapper.map(courseEntity,CourseDTO.class))
                .collect(Collectors.toList());

        return courseDTOS;
    }

    public List<CourseDTO> getAllCoursesOfEducator(){
        List<CourseEntity> courseEntities = courseRepository.findByCategory("Educator");
        List<CourseDTO> courseDTOS = courseEntities.stream()
                .map(courseEntity -> modelMapper.map(courseEntity,CourseDTO.class))
                .collect(Collectors.toList());

        return courseDTOS;
    }

    public List<CourseDTO> getAllCoursesOfProfessional(){
        List<CourseEntity> courseEntities = courseRepository.findByCategory("Professional");
        List<CourseDTO> courseDTOS = courseEntities.stream()
                .map(courseEntity -> modelMapper.map(courseEntity,CourseDTO.class))
                .collect(Collectors.toList());

        return courseDTOS;
    }

    public List<CourseDTO> getAllCoursesOfStudent(){
        List<CourseEntity> courseEntities = courseRepository.findByCategory("Student");
        List<CourseDTO> courseDTOS = courseEntities.stream()
                .map(courseEntity -> modelMapper.map(courseEntity,CourseDTO.class))
                .collect(Collectors.toList());

        return courseDTOS;
    }


    public CourseDTO getCourseById(Long courseId){
        CourseEntity courseEntity = courseRepository.findById(courseId).orElseThrow(()->new CourseNotFoundException("Course not found"));
        return modelMapper.map(courseEntity,CourseDTO.class);
    }


}
