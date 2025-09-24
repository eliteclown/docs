package com.cybercity.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cybercity.application.entities.TeamsSessionEntity;

@Repository
public interface TeamsSessionRepository extends JpaRepository<TeamsSessionEntity,Long> {
	 List<TeamsSessionEntity> findByCourseEntity_CourseId(Long courseId);
}
