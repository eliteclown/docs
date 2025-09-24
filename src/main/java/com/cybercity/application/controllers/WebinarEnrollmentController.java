package com.cybercity.application.controllers;

import com.cybercity.application.dtos.WebinarEnrollmentDTO;
import com.cybercity.application.services.WebinarEnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webinar-enrollments")
@CrossOrigin(origins = "*")
public class WebinarEnrollmentController {

    private final WebinarEnrollmentService enrollmentService;

    public WebinarEnrollmentController(WebinarEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    //  Enroll user into webinar (using email + webinarId)
    @PostMapping("/enroll")
    public WebinarEnrollmentDTO enrollUser(@RequestBody Map<String, Object> request) {
        String email = request.get("email").toString();
        Long webinarId = Long.valueOf(request.get("webinarId").toString());
       

        return enrollmentService.enrollUser(email, webinarId);
    }

    // Get enrollment by ID
    @GetMapping("/{id}")
    public WebinarEnrollmentDTO getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    //  Get all enrollments of a user
    @GetMapping("/user/{userId}")
    public List<WebinarEnrollmentDTO> getEnrollmentsByUser(@PathVariable Long userId) {
        return enrollmentService.getEnrollmentsByUser(userId);
    }

    //  Get all enrollments of a webinar
    @GetMapping("/webinar/{webinarId}")
    public List<WebinarEnrollmentDTO> getEnrollmentsByWebinar(@PathVariable Long webinarId) {
        return enrollmentService.getEnrollmentsByWebinar(webinarId);
    }

    //  Cancel enrollment
    @DeleteMapping("/{id}")
    public String cancelEnrollment(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return "Enrollment cancelled with id " + id;
    }
}
