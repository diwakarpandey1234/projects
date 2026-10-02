package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {

    @NotBlank(message = "Employee cannot be without ID")
    private Long employeeID;
    @NotBlank(message = "firstName cannot be null")
    private String firstName;
    private String lastName;
    @NotBlank(message = "email cannot be null")
    private String email;
    private String designation;
    private String departmentName;
    private List<SalaryResponseDTO> salaries;
}