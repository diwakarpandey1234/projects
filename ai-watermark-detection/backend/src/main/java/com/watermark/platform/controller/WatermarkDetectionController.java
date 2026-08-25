package com.watermark.platform.controller;

import com.watermark.platform.dto.TextDetectionRequest;
import com.watermark.platform.dto.WatermarkDetectionResponse;
import com.watermark.platform.entity.User;
import com.watermark.platform.service.WatermarkDetectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/detect")
@RequiredArgsConstructor
public class WatermarkDetectionController {

    private final WatermarkDetectionService detectionService;

    @PostMapping("/text")
    public ResponseEntity<WatermarkDetectionResponse> detectText(
            @Valid @RequestBody TextDetectionRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(detectionService.detectTextWatermark(request.getText(), currentUser));
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WatermarkDetectionResponse> detectImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Media file is missing or empty.");
        }

        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(detectionService.detectImageWatermark(file, currentUser));
    }
}
