package hello.hackathon.domain.record.service;

import hello.hackathon.domain.record.dto.RecordRequestDto;
import hello.hackathon.domain.record.dto.RecordResponseDto;
import hello.hackathon.domain.record.entity.Record;
import hello.hackathon.domain.record.entity.enums.EmotionType;
import hello.hackathon.domain.record.repository.RecordRepository;
import hello.hackathon.global.gpt.GptService;
import hello.hackathon.global.tts.TtsService;
import hello.hackathon.global.emotion.EmotionAnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final GptService gptService;
    private final TtsService ttsService;
    private final EmotionAnalyzerService emotionAnalyzerService;

    // 기록 생성
    public RecordResponseDto createRecord(RecordRequestDto requestDto) {
        String diary = requestDto.getDiaryContent();

        EmotionType emotionType = emotionAnalyzerService.analyze(diary);
        String feedback = gptService.generateFeedback(diary);
        String voiceUrl = ttsService.generateVoiceUrl(feedback);
        String thumbnail = generateThumbnail(emotionType.name());

        Record record = Record.builder()
                .userId(requestDto.getUserId())
                .title(requestDto.getTitle())
                .diaryContent(diary)
                .feedbackContent(feedback)
                .voiceUrl(voiceUrl)
                .emotionType(emotionType)
                .thumbnailImage(thumbnail)
                .createdAt(LocalDateTime.now())
                .build();

        return RecordResponseDto.fromEntity(recordRepository.save(record));
    }

    // 단일 조회
    public RecordResponseDto getRecordById(Long id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다: " + id));
        return RecordResponseDto.fromEntity(record);
    }

    // 유저별 전체 조회
    public List<RecordResponseDto> getRecordsByUser(Long userId) {
        return recordRepository.findAllByUserId(userId).stream()
                .map(RecordResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 삭제 (Soft delete 고려한 구조로 변경)
    public void deleteRecord(Long id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 기록이 존재하지 않습니다: " + id));
        record.setDeletedAt(LocalDateTime.now());
        recordRepository.save(record);
    }

    // 감정 기반 썸네일 매핑
    private String generateThumbnail(String emotionType) {
        return "https://cdn.triography.app/emotions/" + emotionType.toLowerCase() + ".png";
    }
}
