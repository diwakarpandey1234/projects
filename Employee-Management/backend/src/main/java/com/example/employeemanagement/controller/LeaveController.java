package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.LeaveResponseDto;
import com.example.employeemanagement.dto.LeaveRequestDto;
import com.example.employeemanagement.entity.LeaveStatus;
import com.example.employeemanagement.entity.LeaveType;
import com.example.employeemanagement.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }



    // APPLY FOR LEAVE


    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('LEAVE_APPLY')")
    public ResponseEntity<LeaveResponseDto> applyLeave(
            @Valid @RequestBody LeaveRequestDto leaveDTO) {

        LeaveResponseDto response =
                leaveService.applyLeave(leaveDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }



    // GET LEAVE BY ID


    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasAuthority('LEAVE_READ')")
    public ResponseEntity<LeaveResponseDto> getLeaveById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService.getLeaveById(id)
        );
    }



    // GET EMPLOYEE LEAVES


    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_READ')")
    public ResponseEntity<List<LeaveResponseDto>> getEmployeeLeaves(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                leaveService.getEmployeeLeaves(employeeId)
        );
    }






    // GET PENDING LEAVES


    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<LeaveResponseDto>> getPendingLeaves() {

        return ResponseEntity.ok(
                leaveService.getPendingLeaves()
        );
    }



    // APPROVE LEAVE



    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<LeaveResponseDto> approveLeave(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService.approveLeave(id)
        );
    }



    // REJECT LEAVE

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('LEAVE_REJECT')")
    public ResponseEntity<LeaveResponseDto> rejectLeave(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService.rejectLeave(id)
        );
    }



    // CANCEL LEAVE

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('LEAVE_CANCEL')")
    public ResponseEntity<LeaveResponseDto> cancelLeave(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService.cancelLeave(id)
        );
    }



    // GET BY STATUS

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('LEAVE_READ')")
    public ResponseEntity<List<LeaveResponseDto>> getLeavesByStatus(
            @PathVariable LeaveStatus status) {

        return ResponseEntity.ok(
                leaveService.getLeavesByStatus(status)
        );
    }



    // GET BY TYPE


    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('LEAVE_READ')")
    public ResponseEntity<List<LeaveResponseDto>> getLeavesByType(
            @PathVariable LeaveType type) {

        return ResponseEntity.ok(
                leaveService.getLeavesByType(type)
        );
    }
}