package hello.hackathon.domain.record.repository;

import hello.hackathon.domain.record.entity.Record;
import hello.hackathon.domain.record.entity.enums.EmotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    // 특정 유저의 기록 전체 조회
    List<Record> findAllByUserId(Long userId);

    // (선택) 감정별 필터링도 가능
    List<Record> findAllByUserIdAndEmotionType(Long userId, EmotionType emotionType);
}
