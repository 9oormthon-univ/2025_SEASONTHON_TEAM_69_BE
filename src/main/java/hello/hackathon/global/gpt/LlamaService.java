package hello.hackathon.global.gpt;

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
public class LlamaService {

    private final @Qualifier("ollamaWebClient") WebClient webClient; // ✅ 이름 맞춰 주입

    @Value("${ollama.model}")
    private String model;

    private static final String SYSTEM_PROMPT = """
        너는 한국어 심리 상담사야. 반드시 아래 규칙을 지켜라.
        1) 반드시 한국어로만 답한다.
        2) 따뜻하고 자연스러운 어조를 사용한다. 기계 번역투를 피한다.
        3) 전체 분량은 3~4문장, 120~180자 이내로 한다.
        4) 영어/한자/외래어/로마자 표기는 금지한다. 숫자·기호는 허용한다.
        5) 첫 문장은 "<사용자 이름>님," 으로 시작할 수 있다면 그렇게 시작한다.
        """;

    public String chatWithPrompt(String diaryContent, String userName) {
        try {
            String safeName = (userName == null || userName.isBlank()) ? "친구" : userName.trim();

            var body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", buildUserContent(diaryContent, safeName))
                    ),
                    "stream", false
            );

            Map<?, ?> resp = webClient.post()
                    .uri("/api/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(120))
                    .block();

            if (resp == null || resp.get("message") == null) return "";
            var message = (Map<?, ?>) resp.get("message");
            var content = message.get("content");
            return content == null ? "" : content.toString().trim();

        } catch (Exception e) {
            return "[오류] Ollama 응답을 가져오지 못했습니다.";
        }
    }

    private String buildUserContent(String diary, String name) {
        return """
                [사용자 정보]
                이름: %s

                [일기]
                %s

                [작성 지침]
                - 첫 문장은 "%s님," 으로 시작.
                - 일기 속 감정을 짧게 반영.
                - 작게 시도할 수 있는 구체적 행동 1~2개 제안.
                - 마지막은 짧은 격려로 마무리.
                """.formatted(name, diary, name);
    }
}
