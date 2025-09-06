package hello.hackathon.domain.record.controller;

import hello.hackathon.domain.record.dto.RecordRequestDto;
import hello.hackathon.domain.record.dto.RecordResponseDto;
import hello.hackathon.domain.record.service.RecordService;
import hello.hackathon.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final S3Uploader s3Uploader;

    /**
     * 🆕 기록 생성 (영상 + 썸네일 이미지 포함)
     * 프론트에서:
     * - request: JSON (RecordRequestDto)
     * - video: MultipartFile
     * - thumbnail: MultipartFile
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecordResponseDto> createRecord(
            @RequestPart("request") RecordRequestDto requestDto,
            @RequestPart(value = "video", required = false) MultipartFile videoFile,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage
    ) throws IOException {
        // 🎥 영상 S3 업로드
        if (videoFile != null && !videoFile.isEmpty()) {
            String videoUrl = s3Uploader.upload(videoFile, "videos");
            requestDto.setVideoUrl(videoUrl);
        }

        // 🖼️ 썸네일 S3 업로드
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            String thumbnailUrl = s3Uploader.upload(thumbnailImage, "thumbnails");
            requestDto.setThumbnailImage(thumbnailUrl);
        }

        RecordResponseDto response = recordService.createRecord(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecordResponseDto> getRecord(@PathVariable Long id) {
        RecordResponseDto response = recordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 유저별 전체 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecordResponseDto>> getRecordsByUser(@PathVariable Long userId) {
        List<RecordResponseDto> responseList = recordService.getRecordsByUser(userId);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
