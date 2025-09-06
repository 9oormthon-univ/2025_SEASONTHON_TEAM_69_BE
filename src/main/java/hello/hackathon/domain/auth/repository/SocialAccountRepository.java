package hello.hackathon.domain.auth.repository;

import hello.hackathon.domain.auth.entity.SocialAccount;
import hello.hackathon.domain.auth.entity.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findByProviderAndProviderId(SocialProvider provider, String providerId);
}
