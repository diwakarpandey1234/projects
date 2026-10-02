package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditResponseDTO {
    private Long id;
    private String username;
    private String action;
    private String entity;
    private Long entityId;
    private LocalDateTime timestamp;
    private String details;
}
