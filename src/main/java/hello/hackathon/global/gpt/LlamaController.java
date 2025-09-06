package hello.hackathon.global.gpt;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/llama")
public class LlamaController {
    private final LlamaService llamaService;

    // 위로 메시지 생성 테스트
    @PostMapping("/cheer")
    public String cheer(@RequestBody CheerRequest req) {
        return llamaService.chatWithPrompt(req.getDiaryContent(), req.getUserName());
    }

    // 요청 DTO
    @Data
    public static class CheerRequest {
        private String diaryContent; // 일기 내용
        private String userName;     // 사용자 이름 (없으면 "친구"로 처리됨)
    }
}
