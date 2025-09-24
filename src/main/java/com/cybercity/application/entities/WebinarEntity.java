package com.cybercity.application.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "webinars")
public class WebinarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long webinarId;

    private String title;
    private String speaker;
    private String teamChatLink;
    private String image;

    @Column(length = 2000)
    private String description;

    private String webinarDate;

  
  

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "webinarEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WebinarEnrollmentEntity> webinarEnrollments;
}
