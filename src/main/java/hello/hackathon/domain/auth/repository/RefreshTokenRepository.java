package hello.hackathon.domain.auth.repository;

import hello.hackathon.domain.auth.entity.RefreshToken;
import hello.hackathon.domain.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findTopByUserEntityOrderByIssuedAtDesc(UserEntity u);

    Optional<RefreshToken> findByUserEntityAndToken(UserEntity userEntity, String token);

    Optional<RefreshToken> findByTokenAndUserEntity(String token, UserEntity user);

    List<RefreshToken> findAllByUserEntityAndRevokedAtIsNullAndExpiresAtAfter(UserEntity user, Instant now);

    long deleteByUserEntityAndExpiresAtBefore(UserEntity user, Instant time);
}
