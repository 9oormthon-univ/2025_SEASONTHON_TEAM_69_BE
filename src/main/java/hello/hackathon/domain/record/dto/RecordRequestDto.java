package hello.hackathon.domain.record.dto;

import hello.hackathon.domain.auth.dto.KakaoProfileDto;
import hello.hackathon.domain.record.entity.Record;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordRequestDto {

    private Long userId;
    private String title;
    private String diaryContent;
    private String thumbnailImage;

    private KakaoProfileDto kakaoProfile;

    // DTO → Entity 변환
    public Record toEntity() {
        return Record.builder()
                .userId(userId)
                .title(title)
                .diaryContent(diaryContent)
                .thumbnailImage(thumbnailImage)
                .build();
    }
}
