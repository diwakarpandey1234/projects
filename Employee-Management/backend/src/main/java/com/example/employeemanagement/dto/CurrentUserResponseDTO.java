package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponseDTO {

    private Long userId;
    private String username;
    private String role;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String designation;
    private String status;
}
