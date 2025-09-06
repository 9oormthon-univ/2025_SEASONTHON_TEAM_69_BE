package hello.hackathon.global.tts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tts")
public class TtsController {

    private final TtsService ttsService;

    @PostMapping
    public ResponseEntity<String> generateVoice(@RequestBody TtsRequest request) {
        String s3Url = ttsService.generateVoiceUrl(
                request.getText(),
                request.getGender(),
                request.getRate(),
                request.getPitch()
        );

        return ResponseEntity.ok(s3Url);
    }
}
