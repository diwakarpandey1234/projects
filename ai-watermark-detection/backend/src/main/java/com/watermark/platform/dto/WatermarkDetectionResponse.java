package com.watermark.platform.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class WatermarkDetectionResponse {
    private boolean isWatermarked;
    private String detectionMethod;
    private double confidenceScore;
    private Map<String, Object> statisticalMetrics;
    private Integer remainingTokens;
    private String message;
}
