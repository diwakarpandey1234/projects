package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.LeaveRequestDto;
import com.example.employeemanagement.dto.LeaveResponseDto;
import com.example.employeemanagement.entity.*;
import com.example.employeemanagement.exception.InvalidLeaveException;
import com.example.employeemanagement.exception.LeaveNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.LeaveRepository;
import com.example.employeemanagement.repository.userDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private userDetailsRepository userRepository;

    @InjectMocks
    private LeaveService leaveService;


    @BeforeEach
    void setUp() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "admin",
                        "password",
                        List.of()
                )
        );
    }


    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    // ==========================================
    // APPLY LEAVE
    // ==========================================

    @Test
    void shouldApplyLeaveSuccessfully() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");

        LeaveRequestDto dto = new LeaveRequestDto();

        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CASUAL);
        dto.setStartDate(LocalDate.of(2026, 10, 1));
        dto.setEndDate(LocalDate.of(2026, 10, 3));
        dto.setReason("Personal work");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRepository.findByEmployeeId(1L))
                .thenReturn(List.of());

        when(leaveRepository.save(any(Leave.class)))
                .thenAnswer(invocation -> {

                    Leave leave = invocation.getArgument(0);
                    leave.setId(10L);
                    return leave;
                });

        when(userRepository.findByRole(Role.ADMIN))
                .thenReturn(List.of());

        when(userRepository.findByRole(Role.HR))
                .thenReturn(List.of());

        LeaveResponseDto result =
                leaveService.applyLeave(dto);

        assertNotNull(result);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getEmployeeId());
        assertEquals("xyz uvw", result.getEmployeeName());
        assertEquals(LeaveType.CASUAL, result.getLeaveType());
        assertEquals(
                LocalDate.of(2026, 10, 1),
                result.getStartDate()
        );
        assertEquals(
                LocalDate.of(2026, 10, 3),
                result.getEndDate()
        );
        assertEquals(
                LeaveStatus.PENDING,
                result.getStatus()
        );

        verify(leaveRepository)
                .save(any(Leave.class));

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("LEAVE_APPLIED"),
                        eq("Leave"),
                        eq(10L),
                        eq("Leave applied for employee 1")
                );
    }


    // ==========================================
    // INVALID DATES
    // ==========================================

    @Test
    void shouldRejectLeaveWhenStartDateIsAfterEndDate() {

        LeaveRequestDto dto = new LeaveRequestDto();

        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CASUAL);

        dto.setStartDate(
                LocalDate.of(2026, 10, 10)
        );

        dto.setEndDate(
                LocalDate.of(2026, 10, 5)
        );

        assertThrows(
                InvalidLeaveException.class,
                () -> leaveService.applyLeave(dto)
        );

        verify(employeeRepository, never())
                .findById(anyLong());

        verify(leaveRepository, never())
                .save(any(Leave.class));
    }


    @Test
    void shouldRejectLeaveWhenDatesAreNull() {

        LeaveRequestDto dto = new LeaveRequestDto();

        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.CASUAL);

        dto.setStartDate(null);
        dto.setEndDate(null);

        assertThrows(
                InvalidLeaveException.class,
                () -> leaveService.applyLeave(dto)
        );

        verify(employeeRepository, never())
                .findById(anyLong());
    }


    // ==========================================
    // EMPLOYEE NOT FOUND
    // ==========================================

    @Test
    void shouldRejectLeaveWhenEmployeeDoesNotExist() {

        LeaveRequestDto dto = new LeaveRequestDto();

        dto.setEmployeeId(99L);
        dto.setLeaveType(LeaveType.CASUAL);

        dto.setStartDate(
                LocalDate.of(2026, 10, 1)
        );

        dto.setEndDate(
                LocalDate.of(2026, 10, 3)
        );

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidLeaveException.class,
                () -> leaveService.applyLeave(dto)
        );

        verify(leaveRepository, never())
                .save(any(Leave.class));
    }


    // ==========================================
    // OVERLAPPING APPROVED LEAVE
    // ==========================================

    @Test
    void shouldRejectOverlappingApprovedLeave() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");

        Leave existingLeave = new Leave();

        existingLeave.setId(5L);
        existingLeave.setEmployee(employee);
        existingLeave.setLeaveType(LeaveType.CASUAL);

        existingLeave.setStartDate(
                LocalDate.of(2026, 10, 2)
        );

        existingLeave.setEndDate(
                LocalDate.of(2026, 10, 5)
        );

        existingLeave.setStatus(
                LeaveStatus.APPROVED
        );

        LeaveRequestDto dto = new LeaveRequestDto();

        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.SICK);

        dto.setStartDate(
                LocalDate.of(2026, 10, 4)
        );

        dto.setEndDate(
                LocalDate.of(2026, 10, 7)
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRepository.findByEmployeeId(1L))
                .thenReturn(List.of(existingLeave));

        assertThrows(
                InvalidLeaveException.class,
                () -> leaveService.applyLeave(dto)
        );

        verify(leaveRepository, never())
                .save(any(Leave.class));
    }


    // ==========================================
    // APPROVE LEAVE
    // ==========================================

    @Test
    void shouldApprovePendingLeave() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");

        User user = new User();
        user.setId(2L);

        employee.setUser(user);

        Leave leave = new Leave();

        leave.setId(10L);
        leave.setEmployee(employee);
        leave.setLeaveType(LeaveType.CASUAL);

        leave.setStartDate(
                LocalDate.of(2026, 10, 1)
        );

        leave.setEndDate(
                LocalDate.of(2026, 10, 3)
        );

        leave.setStatus(
                LeaveStatus.PENDING
        );

        when(leaveRepository.findById(10L))
                .thenReturn(Optional.of(leave));

        when(leaveRepository.findByEmployeeId(1L))
                .thenReturn(List.of(leave));

        when(leaveRepository.save(any(Leave.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        LeaveResponseDto result =
                leaveService.approveLeave(10L);

        assertEquals(
                LeaveStatus.APPROVED,
                result.getStatus()
        );

        verify(leaveRepository)
                .save(leave);

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("LEAVE_APPROVED"),
                        eq("Leave"),
                        eq(10L),
                        eq("Leave approved for employee 1")
                );

        verify(notificationService)
                .sendNotification(
                        eq(2L),
                        eq("Leave Approved"),
                        eq("Your leave request has been approved."),
                        eq(NotificationType.LEAVE)
                );
    }


    // ==========================================
    // APPROVE NON-PENDING LEAVE
    // ==========================================

    @Test
    void shouldNotApproveAlreadyApprovedLeave() {

        Employee employee = new Employee();
        employee.setId(1L);

        Leave leave = new Leave();

        leave.setId(10L);
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.APPROVED);

        when(leaveRepository.findById(10L))
                .thenReturn(Optional.of(leave));

        assertThrows(
                InvalidLeaveException.class,
                () -> leaveService.approveLeave(10L)
        );

        verify(leaveRepository, never())
                .save(any(Leave.class));
    }


    // ==========================================
    // REJECT LEAVE
    // ==========================================

    @Test
    void shouldRejectPendingLeave() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");

        User user = new User();
        user.setId(2L);

        employee.setUser(user);

        Leave leave = new Leave();

        leave.setId(20L);
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveRepository.findById(20L))
                .thenReturn(Optional.of(leave));

        when(leaveRepository.save(any(Leave.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        LeaveResponseDto result =
                leaveService.rejectLeave(20L);

        assertEquals(
                LeaveStatus.REJECTED,
                result.getStatus()
        );

        verify(leaveRepository)
                .save(leave);

        verify(notificationService)
                .sendNotification(
                        eq(2L),
                        eq("Leave Rejected"),
                        eq("Your leave request has been rejected."),
                        eq(NotificationType.LEAVE)
                );
    }


    // ==========================================
    // CANCEL LEAVE
    // ==========================================

    @Test
    void shouldCancelPendingLeave() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");

        Leave leave = new Leave();

        leave.setId(30L);
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveRepository.findById(30L))
                .thenReturn(Optional.of(leave));

        when(leaveRepository.save(any(Leave.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        LeaveResponseDto result =
                leaveService.cancelLeave(30L);

        assertEquals(
                LeaveStatus.CANCELLED,
                result.getStatus()
        );

        verify(leaveRepository)
                .save(leave);

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("LEAVE_CANCELLED"),
                        eq("Leave"),
                        eq(30L),
                        eq("Leave cancelled for employee 1")
                );
    }


    // ==========================================
    // LEAVE NOT FOUND
    // ==========================================

    @Test
    void shouldThrowExceptionWhenLeaveDoesNotExist() {

        when(leaveRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.getLeaveById(999L)
        );
    }


    // ==========================================
    // GET PENDING LEAVES
    // ==========================================

    @Test
    void shouldReturnPendingLeaves() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");

        Leave leave = new Leave();

        leave.setId(1L);
        leave.setEmployee(employee);
        leave.setLeaveType(LeaveType.CASUAL);
        leave.setStartDate(
                LocalDate.of(2026, 10, 1)
        );
        leave.setEndDate(
                LocalDate.of(2026, 10, 3)
        );
        leave.setStatus(
                LeaveStatus.PENDING
        );

        when(leaveRepository.findByStatus(
                LeaveStatus.PENDING
        )).thenReturn(List.of(leave));

        List<LeaveResponseDto> result =
                leaveService.getPendingLeaves();

        assertEquals(1, result.size());

        assertEquals(
                LeaveStatus.PENDING,
                result.get(0).getStatus()
        );

        assertEquals(
                "xyz uvw",
                result.get(0).getEmployeeName()
        );
    }
}