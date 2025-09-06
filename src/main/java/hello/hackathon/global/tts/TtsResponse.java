package hello.hackathon.global.tts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TtsResponse {
    private String voiceUrl;
    private String voiceName;
}
