package hello.hackathon.domain.auth.repository;

import hello.hackathon.domain.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
