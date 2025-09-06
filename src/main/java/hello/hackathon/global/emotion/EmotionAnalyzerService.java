package hello.hackathon.global.emotion;

import hello.hackathon.domain.record.entity.enums.EmotionType;
import hello.hackathon.global.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionAnalyzerService {

    private final GptService gptService;

    public EmotionType analyze(String diaryContent) {
        // 프롬프트 구성
        String prompt = buildPrompt(diaryContent);

        // GPT 응답 (단답형 감정 이름 받아온다고 가정)
        String result = gptService.ask(prompt).trim().toUpperCase();

        try {
            return EmotionType.valueOf(result);
        } catch (IllegalArgumentException e) {
            // 못 알아들었으면 기본값 NEUTRAL
            return EmotionType.NEUTRAL;
        }
    }

    // 프롬프트 템플릿
    private String buildPrompt(String diaryContent) {
        return """
                아래 일기 내용을 읽고 감정을 분석해줘.
                감정은 아래 보기 중 하나로 단답형으로만 대답해.

                [HAPPY, SAD, ANGRY, NEUTRAL, ANXIOUS, EXCITED, LONELY]

                일기 내용:
                %s

                감정:
                """.formatted(diaryContent);
    }
}
