package com.example.messengerapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;


}
