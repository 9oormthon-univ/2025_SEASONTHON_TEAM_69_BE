package hello.hackathon.domain.record.controller;

import hello.hackathon.domain.record.dto.RecordRequestDto;
import hello.hackathon.domain.record.dto.RecordResponseDto;
import hello.hackathon.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    // 기록 생성
    @PostMapping
    public ResponseEntity<RecordResponseDto> createRecord(@RequestBody RecordRequestDto requestDto) {
        RecordResponseDto response = recordService.createRecord(requestDto);
        return ResponseEntity.ok(response);
    }

    // 기록 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<RecordResponseDto> getRecord(@PathVariable Long id) {
        RecordResponseDto response = recordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }

    // 유저의 전체 기록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecordResponseDto>> getRecordsByUser(@PathVariable Long userId) {
        List<RecordResponseDto> responseList = recordService.getRecordsByUser(userId);
        return ResponseEntity.ok(responseList);
    }

    // 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
