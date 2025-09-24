package com.cybercity.application.controllers;

import com.cybercity.application.dtos.EnrollmentDTO;
import com.cybercity.application.services.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enroll")
@CrossOrigin(origins = "*")

public class EnrollmentConroller {
    private final EnrollmentService enrollmentService;

    public EnrollmentConroller(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/new")
    public EnrollmentDTO enrollNewCourse(
            @RequestParam String email,
            @RequestParam Long courseId,
            @RequestParam String category) {

        return enrollmentService.enrollNewUser(email, courseId, category);
    }

    @GetMapping("/get/{userId}")
    public List<EnrollmentDTO> getEnrolledCourseByUserId(@PathVariable Long userId){
        return enrollmentService.courseEnrolledByUserId(userId);
    }

}
