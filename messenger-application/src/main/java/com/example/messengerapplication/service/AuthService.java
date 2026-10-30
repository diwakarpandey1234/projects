package com.example.messengerapplication.service;

import com.example.messengerapplication.DTO.LoginRequestDTO;
import com.example.messengerapplication.DTO.LoginResponseDTO;
import com.example.messengerapplication.DTO.RegisterRequest;
import com.example.messengerapplication.DTO.RegisterResponse;
import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@Service
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {

        String username = registerRequest.getUsername().trim();
        String email = registerRequest.getEmail().trim().toLowerCase();

        if (username.isEmpty() || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Username and Email are required");
        }

        if (userRepository.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists");
        }

        if (userRepository.findByUsernameIgnoreCase(username) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username already exists");
        }

        String password = registerRequest.getPassword();

        if (password.length() < 3 ||
                password.getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password length exceeded");
        }

        User userEntity = new User();
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.saveAndFlush(userEntity);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {


        String username = requestDTO.getUsername().trim();
        String password = requestDTO.getPassword().trim();

        if (username.isEmpty() || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Username and Password are required");
        }

        User user = userRepository.findByUsernameIgnoreCase(username);


        boolean passwordMatches = passwordEncoder.matches(
                requestDTO.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponseDTO(
                token,
                900,
                user.getUsername()
        );

    }
}
