package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.LeaveRequestDto;
import com.example.employeemanagement.dto.LeaveRequestDto;
import com.example.employeemanagement.dto.LeaveResponseDto;
import com.example.employeemanagement.dto.LeaveResponseDto;
import com.example.employeemanagement.entity.*;
import com.example.employeemanagement.exception.InvalidLeaveException;
import com.example.employeemanagement.exception.LeaveNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRepository;
import com.example.employeemanagement.repository.userDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.entity.NotificationType;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final userDetailsRepository userRepository;

    public LeaveService(
            LeaveRepository leaveRepository,
            EmployeeRepository employeeRepository,
            AuditService auditService,
            NotificationService notificationService,
            userDetailsRepository userRepository) {

        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.auditService=auditService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }




    // =========================
    // APPLY LEAVE
    // =========================

    @Transactional
    public LeaveResponseDto applyLeave(LeaveRequestDto dto) {

        validateDates(dto.getStartDate(), dto.getEndDate());

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new InvalidLeaveException(
                                "Employee not found with id: " + dto.getEmployeeId()
                        ));

        // Check overlapping approved leave
        List<Leave> existingLeaves =
                leaveRepository.findByEmployeeId(employee.getEmployeeID());

        for (Leave leave : existingLeaves) {

            if (leave.getStatus() != LeaveStatus.APPROVED) {
                continue;
            }

            boolean overlaps =
                    !dto.getEndDate().isBefore(leave.getStartDate())
                            &&
                            !dto.getStartDate().isAfter(leave.getEndDate());

            if (overlaps) {
                throw new InvalidLeaveException(
                        "Employee already has approved leave between "
                                + leave.getStartDate()
                                + " and "
                                + leave.getEndDate()
                );
            }
        }

        Leave leave = new Leave();

        leave.setEmployee(employee);
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());

        // Every new application starts as PENDING
        leave.setStatus(LeaveStatus.PENDING);

        Leave savedLeave = leaveRepository.save(leave);

        auditService.createAuditLog(
                getCurrentUsername(),
                "LEAVE_APPLIED",
                "Leave",
                savedLeave.getId(),
                "Leave applied for employee "
                        + employee.getEmployeeID()
        );

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        List<User> hrUsers =
                userRepository.findByRole(Role.HR);

        admins.forEach(user ->
                notificationService.sendNotification(
                        user.getEmployee().getEmployeeID(),
                        "New Leave Request",
                        "Employee " + employee.getFirstName()
                                + " has submitted a new leave request.",
                        NotificationType.LEAVE
                )
        );

        hrUsers.forEach(user ->
                notificationService.sendNotification(
                        user.getEmployee().getEmployeeID(),
                        "New Leave Request",
                        "Employee " + employee.getFirstName()
                                + " has submitted a new leave request.",
                        NotificationType.LEAVE
                )
        );

        return convertToResponse(savedLeave);
    }



    // GET LEAVE BY ID


    public LeaveResponseDto getLeaveById(Long id) {

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new LeaveNotFoundException(
                                "Leave not found with id: " + id
                        ));

        return convertToResponse(leave);
    }



    // GET EMPLOYEE LEAVES

    public List<LeaveResponseDto> getEmployeeLeaves(Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new InvalidLeaveException(
                    "Employee not found with id: " + employeeId
            );
        }

        return leaveRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }



    // GET PENDING LEAVES


    public List<LeaveResponseDto> getPendingLeaves() {

        return leaveRepository.findByStatus(LeaveStatus.PENDING)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }



    // APPROVE LEAVE


    @Transactional
    public LeaveResponseDto approveLeave(Long id) {

        Leave leave = getLeaveEntity(id);

        if (leave.getStatus() != LeaveStatus.PENDING) {

            throw new InvalidLeaveException(
                    "Only pending leave can be approved"
            );
        }

        // Double-check overlapping approved leave
        List<Leave> employeeLeaves =
                leaveRepository.findByEmployeeId(
                        leave.getEmployee().getEmployeeID()
                );

        for (Leave existing : employeeLeaves) {

            if (existing.getId().equals(leave.getId())) {
                continue;
            }

            if (existing.getStatus() != LeaveStatus.APPROVED) {
                continue;
            }

            boolean overlaps =
                    !leave.getEndDate().isBefore(existing.getStartDate())
                            &&
                            !leave.getStartDate().isAfter(existing.getEndDate());

            if (overlaps) {
                throw new InvalidLeaveException(
                        "Cannot approve leave because it overlaps "
                                + "with an existing approved leave"
                );
            }
        }

        leave.setStatus(LeaveStatus.APPROVED);

        Leave updatedLeave = leaveRepository.save(leave);
        auditService.createAuditLog(
                getCurrentUsername(),
                "LEAVE_APPROVED",
                "Leave",
                updatedLeave.getId(),
                "Leave approved for employee "
                        + updatedLeave.getEmployee().getEmployeeID()
        );

        User user = leave.getEmployee().getUser();

        notificationService.sendNotification(
                user.getEmployee().getEmployeeID(),
                "Leave Approved",
                "Your leave request has been approved.",
                NotificationType.LEAVE
        );

        return convertToResponse(updatedLeave);
    }



    // REJECT LEAVE


    @Transactional
    public LeaveResponseDto rejectLeave(Long id) {

        Leave leave = getLeaveEntity(id);

        if (leave.getStatus() != LeaveStatus.PENDING) {

            throw new InvalidLeaveException(
                    "Only pending leave can be rejected"
            );
        }

        leave.setStatus(LeaveStatus.REJECTED);

        Leave updatedLeave = leaveRepository.save(leave);
        auditService.createAuditLog(
                getCurrentUsername(),
                "LEAVE_REJECTED",
                "Leave",
                updatedLeave.getId(),
                "Leave rejected for employee "
                        + updatedLeave.getEmployee().getEmployeeID()
        );


        User user = leave.getEmployee().getUser();

        notificationService.sendNotification(
                user.getEmployee().getEmployeeID(),
                "Leave Rejected",
                "Your leave request has been rejected.",
                NotificationType.LEAVE
        );


        return convertToResponse(updatedLeave);
    }



    // CANCEL LEAVE


    @Transactional
    public LeaveResponseDto cancelLeave(Long id) {

        Leave leave = getLeaveEntity(id);

        if (leave.getStatus() != LeaveStatus.PENDING) {

            throw new InvalidLeaveException(
                    "Only pending leave can be cancelled"
            );
        }

        leave.setStatus(LeaveStatus.CANCELLED);

        Leave updatedLeave = leaveRepository.save(leave);

        auditService.createAuditLog(
                getCurrentUsername(),
                "LEAVE_CANCELLED",
                "Leave",
                updatedLeave.getId(),
                "Leave cancelled for employee "
                        + updatedLeave.getEmployee().getEmployeeID()
        );

        return convertToResponse(updatedLeave);
    }



    // GET BY STATUS


    public List<LeaveResponseDto> getLeavesByStatus(
            LeaveStatus status) {

        return leaveRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }



    // GET BY TYPE


    public List<LeaveResponseDto> getLeavesByType(
            LeaveType type) {

        return leaveRepository.findByLeaveType(type)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }



    // HELPER METHODS


    private Leave getLeaveEntity(Long id) {

        return leaveRepository.findById(id)
                .orElseThrow(() ->
                        new LeaveNotFoundException(
                                "Leave not found with id: " + id
                        ));
    }




    private void validateDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {

            throw new InvalidLeaveException(
                    "Start date and end date are required"
            );
        }

        if (startDate.isAfter(endDate)) {

            throw new InvalidLeaveException(
                    "Start date cannot be after end date"
            );
        }
    }




    private LeaveResponseDto convertToResponse(Leave leave) {

        Employee employee = leave.getEmployee();

        String employeeName =
                employee.getFirstName()
                        + " "
                        + (employee.getLastName() == null
                        ? ""
                        : employee.getLastName());

        return new LeaveResponseDto(
                leave.getId(),
                employee.getEmployeeID(),
                employeeName.trim(),
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getStatus()
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
}