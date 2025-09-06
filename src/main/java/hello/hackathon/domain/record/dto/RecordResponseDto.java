package hello.hackathon.domain.record.dto;

import hello.hackathon.domain.record.entity.Record;
import hello.hackathon.domain.record.entity.enums.EmotionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordResponseDto {

    private Long id;
    private Long userId;
    private String title;
    private String diaryContent;
    private String feedbackContent;
    private String voiceUrl;
    private EmotionType emotionType;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private String thumbnailImage;
    private String videoUrl;

    public static RecordResponseDto fromEntity(Record record) {
        return RecordResponseDto.builder()
                .id(record.getId())
                .userId(record.getUserId())
                .title(record.getTitle())
                .diaryContent(record.getDiaryContent())
                .feedbackContent(record.getFeedbackContent())
                .voiceUrl(record.getVoiceUrl())
                .emotionType(record.getEmotionType())
                .createdAt(record.getCreatedAt())
                .deletedAt(record.getDeletedAt())
                .thumbnailImage(record.getThumbnailImage())
                .videoUrl(record.getVideoUrl())
                .build();
    }
}
