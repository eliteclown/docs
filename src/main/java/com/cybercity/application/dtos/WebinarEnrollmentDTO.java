package com.cybercity.application.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebinarEnrollmentDTO {
    private Long enrollmentId;
    private Long userId;
    private Long webinarId;
   
    private LocalDateTime enrolledAt;
}
