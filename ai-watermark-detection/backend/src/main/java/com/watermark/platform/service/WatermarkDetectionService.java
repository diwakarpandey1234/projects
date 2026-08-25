package com.watermark.platform.service;

import com.watermark.platform.dto.FastApiDetectionResult;
import com.watermark.platform.dto.WatermarkDetectionResponse;
import com.watermark.platform.entity.DetectionLog;
import com.watermark.platform.entity.User;
import com.watermark.platform.exception.InsufficientTokensException;
import com.watermark.platform.exception.UnauthorizedMediaException;
import com.watermark.platform.repository.DetectionLogRepository;
import com.watermark.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WatermarkDetectionService {

    private final FastApiClientService fastApiClient;
    private final UserRepository userRepository;
    private final DetectionLogRepository logRepository;

    @Value("${app.tokens.text-cost:1}")
    private int textCost;

    @Value("${app.tokens.image-cost:5}")
    private int imageCost;

    /**
     * FastAPI integration is intentionally disabled until the detector is available.
     * Keep the FastApiClientService in the project so it can be enabled later.
     */
    @Value("${fastapi.enabled:false}")
    private boolean fastApiEnabled;

    @Transactional
    public WatermarkDetectionResponse detectTextWatermark(String text, User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!fastApiEnabled) {
            return demoResponse(managedUser, "Text detection is in demo mode. FastAPI integration is disabled.");
        }


            if (managedUser.getTokenBalance() < textCost) {
                throw new InsufficientTokensException("Insufficient tokens for text analysis. Required: " + textCost);
            }
            managedUser.setTokenBalance(managedUser.getTokenBalance() - textCost);
            userRepository.save(managedUser);


        FastApiDetectionResult result = fastApiClient.callTextDetection(text);

        logRepository.save(DetectionLog.builder()
                .userId(managedUser != null ? managedUser.getId() : null)
                .mediaType("TEXT")
                .isWatermarked(result.isWatermarked())
                .detectionType(result.getMethod())
                .confidenceScore(result.getConfidenceScore())
                .timestamp(LocalDateTime.now())
                .build());

        return WatermarkDetectionResponse.builder()
                .isWatermarked(result.isWatermarked())
                .detectionMethod(result.getMethod())
                .confidenceScore(result.getConfidenceScore())
                .statisticalMetrics(result.getMetrics())
                .remainingTokens(managedUser != null ? managedUser.getTokenBalance() : null)
                .message("Text watermark evaluation completed successfully.")
                .build();
    }

    @Transactional
    public WatermarkDetectionResponse detectImageWatermark(MultipartFile file, User user) {
        if (user == null) {
            throw new UnauthorizedMediaException("Anonymous users can only evaluate text. Please log in to scan images.");
        }


        if (!fastApiEnabled) {
            return demoResponse(user, "Image detection is in demo mode. FastAPI integration is disabled.");
        }

        if (user.getTokenBalance() < imageCost) {
            throw new InsufficientTokensException("Insufficient tokens for image analysis. Required: " + imageCost);
        }

        user.setTokenBalance(user.getTokenBalance() - imageCost);
        userRepository.save(user);

        FastApiDetectionResult result;
        try {
            result = fastApiClient.callImageDetection(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read the uploaded image payload.", e);
        }

        logRepository.save(DetectionLog.builder()
                .userId(user.getId())
                .mediaType("IMAGE")
                .isWatermarked(result.isWatermarked())
                .detectionType(result.getMethod())
                .confidenceScore(result.getConfidenceScore())
                .timestamp(LocalDateTime.now())
                .build());

        return WatermarkDetectionResponse.builder()
                .isWatermarked(result.isWatermarked())
                .detectionMethod(result.getMethod())
                .confidenceScore(result.getConfidenceScore())
                .statisticalMetrics(result.getMetrics())
                .remainingTokens(user.getTokenBalance())
                .message("Image SynthID / pattern extraction completed successfully.")
                .build();
    }

    private WatermarkDetectionResponse demoResponse(User user, String message) {
        return WatermarkDetectionResponse.builder()
                .isWatermarked(false)
                .detectionMethod("DEMO")
                .confidenceScore(0.0)
                .statisticalMetrics(java.util.Map.of("fastApiEnabled", false))
                .remainingTokens(user != null ? user.getTokenBalance() : null)
                .message(message)
                .build();
    }
}
