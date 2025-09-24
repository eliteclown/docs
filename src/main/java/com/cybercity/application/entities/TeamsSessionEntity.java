package com.cybercity.application.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sessions")
public class TeamsSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sessionId;

    @JoinColumn(name = "course_id",referencedColumnName = "courseId")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity courseEntity;

    private String title;
    private  String description;


    private String startDate;
    private String duration;
    private String teamsId;
    private String joinUrl;
    private String recordingUrl;
}
