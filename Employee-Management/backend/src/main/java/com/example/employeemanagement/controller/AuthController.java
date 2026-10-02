package com.example.employeemanagement.controller;


import com.example.employeemanagement.dto.RefreshTokenResponse;
import com.example.employeemanagement.entity.AuthRequest;
import com.example.employeemanagement.entity.RefreshToken;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.jwt.JWTUtil;
import com.example.employeemanagement.repository.userDetailsRepository;
import com.example.employeemanagement.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwTutil;
    private final userDetailsRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JWTUtil jwTutil, userDetailsRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwTutil = jwTutil;
        this.userRepository = userRepository;
    }



    @PostMapping("/authenticate")
    public ResponseEntity<RefreshTokenResponse> generateToken(@RequestBody AuthRequest authRequest){
        System.out.println(authRequest.getUsername());

        try {
            System.out.println("hiii");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            User user = userRepository
                    .findByUserName(authRequest.getUsername())
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

            //  Verify selected role
            if (!user.getRole().name().equalsIgnoreCase(authRequest.getRole())) {
                throw new BadCredentialsException(
                        "Invalid role for this user"
                );
            }


            // Short-lived access token
            String accessToken =
                    jwTutil.generateToken(user.getUsername());

            // Long-lived refresh token
            RefreshToken refreshToken =
                    refreshTokenService.createRefreshToken(user);

            return ResponseEntity.ok(
                    new RefreshTokenResponse(
                            accessToken,
                            refreshToken.getToken()
                    )
            );
        }catch (Exception e){
            throw e;

        }
    }

}
