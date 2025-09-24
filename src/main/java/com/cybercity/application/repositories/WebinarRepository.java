package com.cybercity.application.repositories;

import com.cybercity.application.entities.WebinarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebinarRepository extends JpaRepository<WebinarEntity, Long> {
}
