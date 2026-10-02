package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.RefreshTokenRequest;
import com.example.employeemanagement.dto.RefreshTokenResponse;
import com.example.employeemanagement.entity.RefreshToken;
import com.example.employeemanagement.jwt.JWTUtil;
import com.example.employeemanagement.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;

    public RefreshTokenController(
            RefreshTokenService refreshTokenService,
            JWTUtil jwtUtil) {

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