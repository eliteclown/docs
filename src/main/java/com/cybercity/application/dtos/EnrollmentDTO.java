package com.cybercity.application.dtos;

import com.cybercity.application.entities.enums.CompletionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long enrollmentId;
    private Long userId;
    private Long courseId;
    private String category;
    private LocalDateTime enrolledAt;
    private LocalDateTime expiredAt;  // new field
    private Set<CompletionStatus> status;
    private LocalDateTime completedAt;
}
