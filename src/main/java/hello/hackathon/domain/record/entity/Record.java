package hello.hackathon.domain.record.entity;

import hello.hackathon.domain.record.entity.enums.EmotionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String diaryContent;

    @Column(columnDefinition = "TEXT")
    private String feedbackContent;

    private String voiceUrl;

    private String videoUrl; // 🆕 영상 URL 저장

    @Enumerated(EnumType.STRING)
    private EmotionType emotionType;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private String thumbnailImage;
}
