package com.cybercity.application.repositories;

import com.cybercity.application.entities.CourseEntity;
import com.cybercity.application.entities.EnrollmentEntity;
import com.cybercity.application.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity,Long> {
    List<EnrollmentEntity> findByUserEntity(UserEntity userEntity);

    List<EnrollmentEntity> findByCourseEntity(CourseEntity courseEntity);
    boolean existsByUserEntity_UserIdAndCourseEntity_CourseId(Long userId, Long courseId);
}
