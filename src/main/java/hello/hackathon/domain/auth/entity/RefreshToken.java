package hello.hackathon.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //누구의 토큰인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userEntity_id")
    private UserEntity userEntity;

    private String token; //당연히 해시된 상태
    @Column(columnDefinition = "timestamp")
    private Instant issuedAt;
    @Column(columnDefinition = "timestamp")
    private Instant expiresAt;
    @Column(columnDefinition = "timestamp")
    private Instant revokedAt; // 로그아웃/강제폐기 시
    @Column(columnDefinition = "timestamp")
    private Instant rotatedAt; //회전 시

    public void revoke(Instant instant){
        this.revokedAt = instant;
    }

    public void rotate(Instant instant){
        this.rotatedAt = instant;
    }
}
