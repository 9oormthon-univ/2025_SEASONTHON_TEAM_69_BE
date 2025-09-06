package hello.hackathon.global.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    // 위로 메시지 생성용
    public String generateFeedback(String diaryContent) {
        String prompt = String.format("다음 일기에 공감하고 위로하는 한 문장을 만들어줘:\n\"%s\"", diaryContent);
        return ask(prompt);  // 리팩토링: 내부적으로 ask() 호출
    }

    // 공통 GPT 호출 메서드 (EmotionAnalyzerService에서도 사용 가능)
    public String ask(String prompt) {
        // 1. 요청 구성
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        try {
            // 2. GPT 호출
            Map<String, Object> response = restTemplate.postForObject(
                    apiUrl,
                    new HttpEntity<>(request, getHeaders()),
                    Map.class
            );

            // 3. 응답 파싱
            Map<String, Object> choice = ((List<Map<String, Object>>) response.get("choices")).get(0);
            Map<String, String> message = (Map<String, String>) choice.get("message");
            return message.get("content").trim();

        } catch (Exception e) {
            return "NEUTRAL"; // 에러 시 fallback
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
