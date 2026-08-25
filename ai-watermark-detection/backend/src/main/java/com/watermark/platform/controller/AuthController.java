package com.watermark.platform.controller;

import com.watermark.platform.dto.RefreshTokenResponse;
import com.watermark.platform.entity.AuthRequest;
import com.watermark.platform.entity.RefreshToken;
import com.watermark.platform.entity.User;
import com.watermark.platform.jwt.JwtFilter;
import com.watermark.platform.repository.UserRepository;
import com.watermark.platform.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtFilter jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          JwtFilter jwtUtil,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<RefreshTokenResponse> generateToken(@RequestBody AuthRequest authRequest) {
        String email = authRequest.getUsername().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, authRequest.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(new RefreshTokenResponse(accessToken, refreshToken.getToken()));
    }
}
