package com.watermark.platform.controller;

import com.watermark.platform.dto.RefreshTokenRequest;
import com.watermark.platform.dto.RefreshTokenResponse;
import com.watermark.platform.entity.RefreshToken;
import com.watermark.platform.jwt.JwtFilter;
import com.watermark.platform.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtFilter jwtUtil;

    public RefreshTokenController(
            RefreshTokenService refreshTokenService,
            JwtFilter jwtUtil) {

        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService
                        .findByToken(request.getRefreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        String username =
                refreshToken.getUser().getUsername();

        String newAccessToken =
                jwtUtil.generateToken(username);

        return ResponseEntity.ok(
                new RefreshTokenResponse(
                        newAccessToken,
                        refreshToken.getToken()
                )
        );
    }
}