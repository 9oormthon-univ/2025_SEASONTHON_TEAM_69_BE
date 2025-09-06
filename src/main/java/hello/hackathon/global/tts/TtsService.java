package hello.hackathon.global.tts;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import hello.hackathon.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class TtsService {

    private final S3Uploader s3Uploader;

    public String generateVoiceUrl(String text, String gender, double rate, double pitch) {
        String voiceName = selectVoiceByGender(gender);
        System.out.println("🎙 사용하는 voiceName = " + voiceName);

        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {

            // 1. 음성 설정
            VoiceSelectionParams voiceParams = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ko-KR")
                    .setName(voiceName)
                    .build();

            // 2. 오디오 설정
            boolean supportsPitchRate = !voiceName.contains("Chirp3");
            AudioConfig.Builder audioConfigBuilder = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3);

            if (supportsPitchRate) {
                audioConfigBuilder.setSpeakingRate(rate).setPitch(pitch);
            }

            // 3. 입력 텍스트
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // 4. TTS 요청
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(
                    input, voiceParams, audioConfigBuilder.build()
            );

            ByteString audioContents = response.getAudioContent();

            // 5. S3 업로드
            File audioFile = s3Uploader.convertToFile(audioContents.toByteArray());
            String s3Url = s3Uploader.upload(audioFile, "voice");
            audioFile.delete();

            return s3Url;

        } catch (Exception e) {
            throw new RuntimeException("TTS 생성 중 오류 발생", e);
        }
    }

    // 성별에 따라 voice name 반환
    private String selectVoiceByGender(String gender) {
        if ("male".equalsIgnoreCase(gender)) {
            return "ko-KR-Chirp3-HD-alnilam"; // 남성 목소리
        } else {
            return "ko-KR-Chirp3-HD-despina"; // 여성 목소리
        }
    }
}
