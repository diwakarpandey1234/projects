package com.example.employeemanagement.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class DepartmentRequestDTO {

    @NonNull
    private String name;
    @NonNull
    private String location;
}
