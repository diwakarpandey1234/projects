package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DashboardResponseDTO;
import com.example.employeemanagement.entity.AttendanceStatus;
import com.example.employeemanagement.entity.LeaveStatus;
import com.example.employeemanagement.repository.AttendanceRepository;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRepository;
import com.example.employeemanagement.repository.SalaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private SalaryRepository salaryRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void shouldReturnDashboardData() {

        // Employee
        when(employeeRepository.count()).thenReturn(10L);
        when(employeeRepository.countByStatus("ACTIVE")).thenReturn(7L);
        when(employeeRepository.countByStatus("INACTIVE")).thenReturn(3L);

        // Departments
        when(departmentRepository.count()).thenReturn(4L);

        // Leaves
        when(leaveRepository.countByStatus(LeaveStatus.PENDING))
                .thenReturn(2L);

        when(leaveRepository.countByStatus(LeaveStatus.APPROVED))
                .thenReturn(5L);

        when(leaveRepository.countByStatus(LeaveStatus.REJECTED))
                .thenReturn(1L);

        // Attendance
        LocalDate today = LocalDate.now();

        when(attendanceRepository.countByDateAndStatus(
                today,
                AttendanceStatus.PRESENT
        )).thenReturn(6L);

        when(attendanceRepository.countByDateAndStatus(
                today,
                AttendanceStatus.ABSENT
        )).thenReturn(2L);

        when(attendanceRepository.countByDateAndStatus(
                today,
                AttendanceStatus.LATE
        )).thenReturn(1L);

        when(attendanceRepository.countByDateAndStatus(
                today,
                AttendanceStatus.HALF_DAY
        )).thenReturn(1L);

        // Salary
        when(salaryRepository.count()).thenReturn(8L);

        // Execute
        DashboardResponseDTO result =
                dashboardService.getDashboard();

        // Verify
        assertEquals(10L, result.getTotalEmployees());
        assertEquals(7L, result.getActiveEmployees());
        assertEquals(3L, result.getInactiveEmployees());

        assertEquals(4L, result.getTotalDepartments());

        assertEquals(2L, result.getPendingLeaves());
        assertEquals(5L, result.getApprovedLeaves());
        assertEquals(1L, result.getRejectedLeaves());

        assertEquals(6L, result.getTodayPresent());
        assertEquals(2L, result.getTodayAbsent());
        assertEquals(1L, result.getTodayLate());
        assertEquals(1L, result.getTodayHalfDay());

        assertEquals(8L, result.getTotalSalaryRecords());
    }
}