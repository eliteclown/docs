package com.cybercity.application.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long courseId;
    private Long userId;
    private String instructorName;
    private String courseName;
    private List<String> photos;
    private String description;
    private List<String> skills;
    private List<String> curriculum;
    private String trainingDetails;
    private BigDecimal price;
    private String startDate;
    private LocalDateTime createdAt;
    private String category;
}
