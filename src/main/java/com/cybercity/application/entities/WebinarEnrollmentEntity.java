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
@Table(name = "webinar_enrollments")
public class WebinarEnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webinar_id", referencedColumnName = "webinarId")
    private WebinarEntity webinarEntity;

    

    @CreationTimestamp
    private LocalDateTime enrolledAt;

 
}
