package com.cybercity.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cybercity.application.dtos.TeamsSessionDTO;
import com.cybercity.application.repositories.TeamsSessionRepository;

@Service
public class TeamSessionService {
	 private final TeamsSessionRepository teamsSessionRepository;

	    public TeamSessionService(TeamsSessionRepository teamsSessionRepository) {
	        this.teamsSessionRepository = teamsSessionRepository;
	    }

	    public List<TeamsSessionDTO> getSessionsByCourseId(Long courseId) {
	        return teamsSessionRepository.findByCourseEntity_CourseId(courseId)
	                .stream()
	                .map(s -> new TeamsSessionDTO(
	                        s.getSessionId(),
	                        s.getCourseEntity().getCourseId(),
	                        s.getTitle(),
	                        s.getDescription(),
	                        s.getStartDate(),
	                        s.getDuration(),
	                        s.getTeamsId(),
	                        s.getJoinUrl(),
	                        s.getRecordingUrl()
	                ))
	                .collect(Collectors.toList());
	    }

}
