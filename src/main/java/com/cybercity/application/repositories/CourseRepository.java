package com.cybercity.application.repositories;

import com.cybercity.application.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findByCategory(String educator);
}
