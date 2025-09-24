package com.cybercity.application.entities;

import com.cybercity.application.entities.enums.CompletionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "enrollments")
public class EnrollmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long enrollmentId;

    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @JoinColumn(name = "course_id",referencedColumnName ="courseId")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity courseEntity;

    private String category;
    @CreationTimestamp
    private LocalDateTime enrolledAt;

    private LocalDateTime expiredAt;
    
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<CompletionStatus> status;

    private LocalDateTime completedAt;
    
    @PrePersist
    public void setExpiryDate() {
        if (enrolledAt == null) {
            enrolledAt = LocalDateTime.now();
        }
        expiredAt = enrolledAt.plusYears(1); // 1 year after enrollment
    }
}
