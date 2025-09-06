// src/main/java/hello/hackathon/global/emotion/EmotionController.java
package hello.hackathon.global.emotion;

import hello.hackathon.domain.record.entity.enums.EmotionType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emotion")
public class EmotionController {

    private final EmotionAnalyzerService emotionAnalyzerService;

    /**
     * 일기 텍스트를 받아 감정 라벨 하나 반환
     * 응답: "HAPPY" | "SAD" | "ANGRY" | "NEUTRAL" | "ANXIOUS" | "EXCITED" | "LONELY"
     */
    @PostMapping("/classify")
    public EmotionType classify(@RequestBody ClassifyReq req) {
        return emotionAnalyzerService.analyze(req.getDiary());
    }

    @Data
    public static class ClassifyReq {
        private String diary;
    }
}
