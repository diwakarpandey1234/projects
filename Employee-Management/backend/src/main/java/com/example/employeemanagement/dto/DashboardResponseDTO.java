package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {

    // Employee statistics
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;

    // Department statistics
    private long totalDepartments;

    // Leave statistics
    private long pendingLeaves;
    private long approvedLeaves;
    private long rejectedLeaves;

    // Today's attendance
    private long todayPresent;
    private long todayAbsent;
    private long todayLate;
    private long todayHalfDay;

    // Salary statistics
    private long totalSalaryRecords;
}