package com.watermark.platform.service;

import com.watermark.platform.entity.RefreshToken;
import com.watermark.platform.entity.User;
import com.watermark.platform.repository.RefreshTokenRepository;
import com.watermark.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository   userRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public RefreshToken createRefreshToken(User user) {
        User currUser= userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Remove old refresh token
        refreshTokenRepository.deleteByUser(currUser);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());

        // @Value("${jwt.refresh-expiration}")
        long refreshTokenDuration = 604800000;
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDuration)
        );

        refreshToken.setUser(currUser);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {

            refreshTokenRepository.delete(token);

            throw new RuntimeException(
                    "Refresh token has expired"
            );
        }

        return token;
    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Refresh token not found"
                        )
                );
    }

}
