package com.cybercity.application.controllers;

import com.cybercity.application.dtos.CourseDTO;
import com.cybercity.application.services.CourseService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins = "*")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/get/{courseId}")
    public CourseDTO getCourseById(@PathVariable Long courseId){
        return courseService.getCourseById(courseId);
    }

    @GetMapping("/getAll")
    public List<CourseDTO> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GetMapping("/getAll/educator")
    public List<CourseDTO> getAllCoursesOfEducator(){
        return courseService.getAllCoursesOfEducator();
    }

    @GetMapping("/getAll/professional")
    public List<CourseDTO> getAllCoursesOfProfessional(){
        return courseService.getAllCoursesOfProfessional();
    }

    @GetMapping("/getAll/student")
    public List<CourseDTO> getAllCoursesOfStudent(){
        return courseService.getAllCoursesOfStudent();
    }
}
