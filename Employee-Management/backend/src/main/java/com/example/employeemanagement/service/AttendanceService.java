package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.AttendanceResponseDto;
import com.example.employeemanagement.entity.Attendance;
import com.example.employeemanagement.entity.AttendanceStatus;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.AttendanceRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;
    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository,
            AuditService auditService
    ) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.auditService = auditService;
    }



    public AttendanceResponseDto checkIn(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found")
                );

        LocalDate today = LocalDate.now();

        Attendance existingAttendance =
                attendanceRepository
                        .findByEmployeeAndDate(employee, today)
                        .orElse(null);

        if (existingAttendance != null) {
            throw new RuntimeException(
                    "Employee has already checked in today"
            );
        }

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setDate(today);
        attendance.setCheckIn(LocalTime.now());

        /*
         * Example:
         * Check-in after 9:30 AM = LATE
         * Otherwise = PRESENT
         */
        if (attendance.getCheckIn().isAfter(LocalTime.of(9, 30))) {
            attendance.setStatus(AttendanceStatus.LATE);
        } else {
            attendance.setStatus(AttendanceStatus.PRESENT);
        }

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        auditService.createAuditLog(
                getCurrentUsername(),
                "ATTENDANCE_CHECK_IN",
                "Attendance",
                savedAttendance.getId(),
                "Employee " + employeeId +
                        " checked in at " +
                        savedAttendance.getCheckIn()
        );

        return convertToDTO(savedAttendance);
    }



    public AttendanceResponseDto checkOut(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found")
                );

        LocalDate today = LocalDate.now();

        Attendance attendance =
                attendanceRepository
                        .findByEmployeeAndDate(employee, today)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee has not checked in today"
                                )
                        );

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException(
                    "Employee has already checked out"
            );
        }

        attendance.setCheckOut(LocalTime.now());

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        auditService.createAuditLog(
                getCurrentUsername(),
                "ATTENDANCE_CHECK_OUT",
                "Attendance",
                savedAttendance.getId(),
                "Employee " + employeeId +
                        " checked out at " +
                        savedAttendance.getCheckOut()
        );

        return convertToDTO(savedAttendance);
    }




    public List<AttendanceResponseDto> getEmployeeAttendance(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found")
                );

        return attendanceRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }



    public List<AttendanceResponseDto> getMonthlyAttendance(
            Long employeeId,
            int month,
            int year
    ) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found")
                );

        LocalDate startDate =
                LocalDate.of(year, month, 1);

        LocalDate endDate =
                startDate.withDayOfMonth(
                        startDate.lengthOfMonth()
                );

        return attendanceRepository
                .findByEmployeeIdAndDateBetween(
                        employeeId,
                        startDate,
                        endDate
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }



    private AttendanceResponseDto convertToDTO(Attendance attendance) {

        return new AttendanceResponseDto(

                attendance.getEmployee().getEmployeeID(),
                attendance.getDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut(),
                attendance.getStatus().name()
        );
    }




    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "SYSTEM";
        }

        return authentication.getName();
    }




    public List<AttendanceResponseDto> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

}