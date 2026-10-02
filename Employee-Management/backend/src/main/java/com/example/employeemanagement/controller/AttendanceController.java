package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.AttendanceResponseDto;
import com.example.employeemanagement.dto.AttendanceResponseDto;
import com.example.employeemanagement.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService
    ) {
        this.attendanceService = attendanceService;
    }



    @PostMapping("/check-in")
    @PreAuthorize("hasAuthority('ATTENDANCE_WRITE')")
    public ResponseEntity<AttendanceResponseDto> checkIn(
            @RequestParam Long employeeId
    ) {

        return ResponseEntity.ok(
                attendanceService.checkIn(employeeId)
        );
    }



    @PostMapping("/check-out")
    @PreAuthorize("hasAuthority('ATTENDANCE_WRITE')")
    public ResponseEntity<AttendanceResponseDto> checkOut(
            @RequestParam Long employeeId
    ) {

        return ResponseEntity.ok(
                attendanceService.checkOut(employeeId)
        );
    }



    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<List<AttendanceResponseDto>> getEmployeeAttendance(
            @PathVariable Long employeeId
    ) {

        return ResponseEntity.ok(
                attendanceService.getEmployeeAttendance(employeeId)
        );
    }



    @GetMapping("/monthly")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<List<AttendanceResponseDto>> getMonthlyAttendance(
            @RequestParam Long employeeId,
            @RequestParam int month,
            @RequestParam int year
    ) {

        return ResponseEntity.ok(
                attendanceService.getMonthlyAttendance(
                        employeeId,
                        month,
                        year
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<List<AttendanceResponseDto>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance()
        );
    }
}