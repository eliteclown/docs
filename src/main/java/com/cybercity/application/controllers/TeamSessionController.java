package com.cybercity.application.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cybercity.application.dtos.TeamsSessionDTO;
import com.cybercity.application.services.TeamSessionService;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "*")
public class TeamSessionController {
	 private final TeamSessionService teamsSessionService;

	    public TeamSessionController(TeamSessionService teamsSessionService) {
	        this.teamsSessionService = teamsSessionService;
	    }

	    @GetMapping("/course/{courseId}")
	    public List<TeamsSessionDTO> getSessionsByCourseId(@PathVariable Long courseId) {
	        return teamsSessionService.getSessionsByCourseId(courseId);
	    }

}
