package com.cybercity.application.controllers;

import com.cybercity.application.dtos.CourseDTO;
import com.cybercity.application.dtos.TeamsSessionDTO;
import com.cybercity.application.services.AdminService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create/{userId}")
    public CourseDTO createCourse(@RequestBody CourseDTO inputDTO, @PathVariable Long userId){
        return adminService.createNewCourse(inputDTO,userId);
    }

    @PostMapping("/create/livesession/{courseId}")
    public TeamsSessionDTO createTeamsSession(@RequestBody TeamsSessionDTO inputDTO,@PathVariable Long courseId) throws MessagingException {
        return adminService.createNewSession(inputDTO,courseId);
    }
}
