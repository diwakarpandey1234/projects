package com.watermark.platform.repository;

import com.watermark.platform.entity.DetectionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetectionLogRepository extends JpaRepository<DetectionLog, Long> {
    List<DetectionLog> findTop20ByUserIdOrderByTimestampDesc(Long userId);
}
