package com.cybercity.application.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "courses")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long courseId;

    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;
    private String instructorName;
    private String courseName;
    @ElementCollection
    @CollectionTable(name = "course_photos",joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "photo")
    private List<String> photos;
    private String description;
    @ElementCollection
    @CollectionTable(name = "course_skills",joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "skill")
    private List<String> skills;
    @ElementCollection
    @CollectionTable(name = "course_curriculum",joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "curriculum")
    private List<String> curriculum;
    private String trainingDetails;
    private BigDecimal price;
    private String startDate;
    

    @CreationTimestamp
    private LocalDateTime createdAt;
    private String category;

    @OneToMany(mappedBy = "courseEntity")
    private List<TeamsSessionEntity> teamsSessionEntities;

    
}
