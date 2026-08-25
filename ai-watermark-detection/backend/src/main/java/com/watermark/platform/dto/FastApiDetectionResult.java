package com.watermark.platform.dto;

import lombok.Data;
import java.util.Map;

@Data
public class FastApiDetectionResult {
    private boolean watermarked;
    private String method;
    private double confidenceScore;
    private Map<String, Object> metrics;
    private String rawOutput;
}
