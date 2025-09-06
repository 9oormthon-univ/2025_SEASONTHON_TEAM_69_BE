package hello.hackathon.global.emotion;

import hello.hackathon.domain.record.entity.enums.EmotionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmotionAnalyzerService {

    private final @Qualifier("ollamaWebClient") WebClient webClient;

    @Value("${ollama.model}")
    private String model;

    public EmotionType analyze(String diaryText) {
        String systemPrompt = """
            너는 감정 분류기다.
            한국어 일기를 보고 다음 라벨 중 정확히 하나만 대문자로 출력하라.
            [HAPPY, SAD, ANGRY, NEUTRAL, ANXIOUS, EXCITED, LONELY]
            라벨 이외의 추가 설명은 절대 하지 마라.
        """;

        try {
            var body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", "일기:\n" + diaryText)
                    ),
                    "stream", false,
                    "options", Map.of("temperature", 0)
            );

            Map<?, ?> resp = webClient.post()
                    .uri("/api/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            // Ollama 응답에서 message.content 바로 가져오기
            Map<?, ?> message = (Map<?, ?>) resp.get("message");
            String content = (String) message.get("content");

            return EmotionType.valueOf(content.trim().toUpperCase());

        } catch (Exception e) {
            return EmotionType.NEUTRAL; // 실패 시 기본값
        }
    }
}
