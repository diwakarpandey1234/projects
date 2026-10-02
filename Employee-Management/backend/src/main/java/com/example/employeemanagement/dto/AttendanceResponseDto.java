package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponseDto {


    private Long employeeId;

    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String status;
}
