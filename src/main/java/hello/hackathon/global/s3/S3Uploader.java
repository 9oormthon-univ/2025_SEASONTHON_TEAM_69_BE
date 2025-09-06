package hello.hackathon.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 🎙 기존: File 객체(mp3 등) 업로드 (예: TTS)
     */
    public String upload(File file, String dirName) {
        String fileName = generateFileName(dirName, ".mp3");
        amazonS3.putObject(bucket, fileName, file);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * 🛠 byte[] → 임시 파일로 변환
     */
    public File convertToFile(byte[] data) throws IOException {
        File tempFile = File.createTempFile("audio-", ".mp3");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }
        return tempFile;
    }

    /**
     * 🎥 MultipartFile 업로드 (예: 동영상)
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String ext = extractExtension(multipartFile.getOriginalFilename());
        String fileName = generateFileName(dirName, ext);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(bucket, fileName, inputStream, metadata);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * 📦 유틸: 확장자 추출
     */
    private String extractExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ""; // 확장자 없는 경우
    }

    /**
     * 📦 유틸: 디렉토리 + UUID + 확장자 파일명 생성
     */
    private String generateFileName(String dirName, String extension) {
        return dirName + "/" + UUID.randomUUID() + extension;
    }
}
