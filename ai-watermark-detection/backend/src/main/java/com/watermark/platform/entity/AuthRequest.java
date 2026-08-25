package com.watermark.platform.entity;

import lombok.Data;

@Data
public class AuthRequest {
    private String username; // frontend sends the user's email here
    private String password;
}
