package com.watermark.platform.service;

import com.watermark.platform.dto.FastApiDetectionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FastApiClientService {

    private final RestClient fastApiRestClient;

    @Value("${fastapi.endpoints.detect-text}")
    private String detectTextEndpoint;

    @Value("${fastapi.endpoints.detect-image}")
    private String detectImageEndpoint;

    public FastApiDetectionResult callTextDetection(String text) {
        return fastApiRestClient.post()
                .uri(detectTextEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("text", text))
                .retrieve()
                .body(FastApiDetectionResult.class);
    }

    public FastApiDetectionResult callImageDetection(MultipartFile file) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", resource);

        return fastApiRestClient.post()
                .uri(detectImageEndpoint)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(FastApiDetectionResult.class);
    }
}
