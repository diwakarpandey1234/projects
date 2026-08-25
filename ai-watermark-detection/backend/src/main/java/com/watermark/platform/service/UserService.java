package com.watermark.platform.service;

import com.watermark.platform.dto.RegisterUserRequest;
import com.watermark.platform.dto.UserResponse;
import com.watermark.platform.entity.Role;
import com.watermark.platform.entity.User;
import com.watermark.platform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(RegisterUserRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        User user = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(email)
                .username(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .tokenBalance(100)
                .build();

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .tokenBalance(user.getTokenBalance())
                .build();
    }
}
