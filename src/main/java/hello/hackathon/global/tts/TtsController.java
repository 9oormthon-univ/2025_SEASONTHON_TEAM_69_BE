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
    public ResponseEntity<TtsResponse> generateVoice(@RequestBody TtsRequest request) {
        TtsResponse response = ttsService.generateVoiceUrl(request);

        return ResponseEntity.ok(response);
    }
}
