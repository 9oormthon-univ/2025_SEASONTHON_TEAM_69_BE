package hello.hackathon.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드하고 URL 반환
     */
    public String upload(File file, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + ".mp3";
        amazonS3.putObject(bucket, fileName, file);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * byte[] 데이터를 임시 파일로 변환
     */
    public File convertToFile(byte[] data) throws IOException {
        File tempFile = File.createTempFile("audio-", ".mp3");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }
        return tempFile;
    }
}
