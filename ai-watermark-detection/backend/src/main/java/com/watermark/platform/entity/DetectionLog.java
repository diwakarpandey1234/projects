package com.watermark.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "detection_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private String mediaType;

    private Boolean isWatermarked;
    private String detectionType;
    private Double confidenceScore;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
