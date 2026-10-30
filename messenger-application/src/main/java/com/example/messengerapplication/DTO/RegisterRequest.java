package com.example.messengerapplication.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50,
            message = "Username must contain 3 to 50 characters")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "should be in proper format")
    @Size(max=100, message = "Email is too long")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min=3, max=72, message = "Password must contain 3 to 72 characters")
    private String  password;
}
