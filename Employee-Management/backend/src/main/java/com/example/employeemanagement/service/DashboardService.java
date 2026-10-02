package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DashboardResponseDTO;
import com.example.employeemanagement.entity.AttendanceStatus;
import com.example.employeemanagement.entity.LeaveStatus;
import com.example.employeemanagement.repository.AttendanceRepository;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRepository;
import com.example.employeemanagement.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveRepository leaveRepository;
    private final AttendanceRepository attendanceRepository;
    private final SalaryRepository salaryRepository;

    public DashboardService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            LeaveRepository leaveRepository,
            AttendanceRepository attendanceRepository,
            SalaryRepository salaryRepository) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.leaveRepository = leaveRepository;
        this.attendanceRepository = attendanceRepository;
        this.salaryRepository = salaryRepository;
    }





    public DashboardResponseDTO getDashboard() {

        // Employee statistics
        long totalEmployees = employeeRepository.count();

        long activeEmployees =
                employeeRepository.countByStatus("ACTIVE");

        long inactiveEmployees =
                employeeRepository.countByStatus("INACTIVE");


        // Department statistics
        long totalDepartments =
                departmentRepository.count();


        // Leave statistics
        long pendingLeaves =
                leaveRepository.countByStatus(LeaveStatus.PENDING);

        long approvedLeaves =
                leaveRepository.countByStatus(LeaveStatus.APPROVED);

        long rejectedLeaves =
                leaveRepository.countByStatus(LeaveStatus.REJECTED);


        // Today's attendance
        LocalDate today = LocalDate.now();

        long todayPresent =
                attendanceRepository.countByDateAndStatus(
                        today,
                        AttendanceStatus.PRESENT
                );

        long todayAbsent =
                attendanceRepository.countByDateAndStatus(
                        today,
                        AttendanceStatus.ABSENT
                );

        long todayLate =
                attendanceRepository.countByDateAndStatus(
                        today,
                        AttendanceStatus.LATE
                );

        long todayHalfDay =
                attendanceRepository.countByDateAndStatus(
                        today,
                        AttendanceStatus.HALF_DAY
                );


        // Salary statistics
        long totalSalaryRecords =
                salaryRepository.count();


        return new DashboardResponseDTO(
                totalEmployees,
                activeEmployees,
                inactiveEmployees,

                totalDepartments,

                pendingLeaves,
                approvedLeaves,
                rejectedLeaves,

                todayPresent,
                todayAbsent,
                todayLate,
                todayHalfDay,

                totalSalaryRecords
        );
    }
}