package com.cybercity.application.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

	Optional<VerificationToken> findByUserEntity(UserEntity userEntity);
}
