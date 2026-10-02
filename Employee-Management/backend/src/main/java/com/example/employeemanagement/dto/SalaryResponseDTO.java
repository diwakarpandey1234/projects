package com.example.employeemanagement.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalaryResponseDTO {
    private Long id;
    private Long netSalary;
}
