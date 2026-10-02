package com.example.employeemanagement.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}