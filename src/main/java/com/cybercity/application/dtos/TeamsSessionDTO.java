package com.cybercity.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamsSessionDTO {
    private Long sessionId;
    private Long courseId;
    private String title;
    private  String description;
    private String startDate;
    private String duration;
    private String teamsId;
    private String joinUrl;
    private String recordingUrl;
}
