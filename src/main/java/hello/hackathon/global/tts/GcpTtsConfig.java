package hello.hackathon.global.tts;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcpTtsConfig {

    @Bean(destroyMethod = "close")
    public TextToSpeechClient textToSpeechClient() throws Exception {
        return TextToSpeechClient.create(); // gcloud 인증 기반 사용됨
    }
}
