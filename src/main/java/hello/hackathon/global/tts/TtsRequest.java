package hello.hackathon.global.tts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TtsRequest {
    private String text;
    private String gender;
    private double rate = 1.0;
    private double pitch = 0.0;
}
