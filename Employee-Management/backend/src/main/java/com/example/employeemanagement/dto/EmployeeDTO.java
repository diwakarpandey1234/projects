package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    @NotBlank(message = "firstName cannot be null")
    private String firstName;

    private String lastName;

    @Email
    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String status;
    @NotBlank(message = "phone cannot be blank")
    private String phone;
    @NotBlank(message = "designation cannot be blank")
    private String designation;
    @NotBlank(message = "joiningDate cannot be blank")
    private String joiningDate;

    private Long departmentId;

    private Long userId;
}