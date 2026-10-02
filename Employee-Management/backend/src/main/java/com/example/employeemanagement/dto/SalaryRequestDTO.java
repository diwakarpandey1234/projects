package com.example.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class SalaryRequestDTO {

    @NotBlank(message = "basic salary cannot be null")
    private Long basic;
    private Long bonus;
    private Long deduction;
    private Long netSalary;
    @NotBlank(message = "effectiveDate for  salary payment cannot be null")
    private String effectiveDate;


    private EmployeeResponseDTO employeeResponseDTO;
}
