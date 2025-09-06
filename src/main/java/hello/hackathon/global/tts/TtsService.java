package hello.hackathon.global.tts;

import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TtsService {

    // 실제 프로젝트에서는 Google Cloud TTS 연동 필요 (ex: Google SDK 사용)
    // 지금은 간단한 모의 URL 반환하는 형태

    public String generateVoiceUrl(String text) {
        // 예: https://fake-tts-api.com/speak?text=encoded_text
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        return "https://fake-tts-api.com/speak?text=" + encodedText;
    }
}
