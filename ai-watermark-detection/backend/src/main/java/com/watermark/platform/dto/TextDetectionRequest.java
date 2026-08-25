package com.watermark.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TextDetectionRequest {
    @NotBlank(message = "Text input must not be empty or blank")
    @Size(min = 20, message = "Text input must be at least 20 characters for statistical evaluation")
    private String text;
}
