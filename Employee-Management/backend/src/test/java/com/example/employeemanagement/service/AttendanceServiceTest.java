package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.AttendanceResponseDto;
import com.example.employeemanagement.entity.Attendance;
import com.example.employeemanagement.entity.AttendanceStatus;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.AttendanceRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AttendanceService attendanceService;


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
    // CHECK-IN SUCCESS
    // ==========================================

    @Test
    void shouldCheckInEmployeeSuccessfully() {

        Employee employee = new Employee();
        employee.setId(1L);

        LocalDate today = LocalDate.now();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository.findByEmployeeAndDate(
                employee,
                today
        )).thenReturn(Optional.empty());

        when(attendanceRepository.save(any(Attendance.class)))
                .thenAnswer(invocation -> {

                    Attendance attendance =
                            invocation.getArgument(0);

                    attendance.setId(10L);

                    return attendance;
                });

        AttendanceResponseDto result =
                attendanceService.checkIn(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getEmployeeId()
        );

        assertEquals(
                today,
                result.getDate()
        );

        assertNotNull(result.getCheckIn());

        assertNull(result.getCheckOut());

        assertTrue(
                result.getStatus().equals("PRESENT")
                        ||
                        result.getStatus().equals("LATE")
        );

        verify(attendanceRepository)
                .save(any(Attendance.class));

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("ATTENDANCE_CHECK_IN"),
                        eq("Attendance"),
                        eq(10L),
                        contains("Employee 1 checked in at")
                );
    }


    // ==========================================
    // CHECK-IN EMPLOYEE NOT FOUND
    // ==========================================

    @Test
    void shouldNotCheckInWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> attendanceService.checkIn(99L)
                );

        assertEquals(
                "Employee not found",
                exception.getMessage()
        );

        verify(attendanceRepository, never())
                .save(any(Attendance.class));
    }


    // ==========================================
    // DUPLICATE CHECK-IN
    // ==========================================

    @Test
    void shouldNotAllowDuplicateCheckIn() {

        Employee employee = new Employee();
        employee.setId(1L);

        Attendance existingAttendance =
                new Attendance();

        existingAttendance.setId(10L);
        existingAttendance.setEmployee(employee);
        existingAttendance.setDate(LocalDate.now());

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository.findByEmployeeAndDate(
                employee,
                LocalDate.now()
        )).thenReturn(
                Optional.of(existingAttendance)
        );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> attendanceService.checkIn(1L)
                );

        assertEquals(
                "Employee has already checked in today",
                exception.getMessage()
        );

        verify(attendanceRepository, never())
                .save(any(Attendance.class));
    }


    // ==========================================
    // CHECK-OUT SUCCESS
    // ==========================================

    @Test
    void shouldCheckOutEmployeeSuccessfully() {

        Employee employee = new Employee();
        employee.setId(1L);

        Attendance attendance =
                new Attendance();

        attendance.setId(20L);
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(
                LocalTime.of(9, 0)
        );
        attendance.setStatus(
                AttendanceStatus.PRESENT
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository.findByEmployeeAndDate(
                employee,
                LocalDate.now()
        )).thenReturn(
                Optional.of(attendance)
        );

        when(attendanceRepository.save(any(Attendance.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        AttendanceResponseDto result =
                attendanceService.checkOut(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getEmployeeId()
        );

        assertNotNull(
                result.getCheckOut()
        );

        assertEquals(
                "PRESENT",
                result.getStatus()
        );

        verify(attendanceRepository)
                .save(attendance);

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("ATTENDANCE_CHECK_OUT"),
                        eq("Attendance"),
                        eq(20L),
                        contains("Employee 1 checked out at")
                );
    }


    // ==========================================
    // CHECK-OUT WITHOUT CHECK-IN
    // ==========================================

    @Test
    void shouldNotCheckOutWithoutCheckIn() {

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository.findByEmployeeAndDate(
                employee,
                LocalDate.now()
        )).thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> attendanceService.checkOut(1L)
                );

        assertEquals(
                "Employee has not checked in today",
                exception.getMessage()
        );

        verify(attendanceRepository, never())
                .save(any(Attendance.class));
    }


    // ==========================================
    // DUPLICATE CHECK-OUT
    // ==========================================

    @Test
    void shouldNotAllowDuplicateCheckOut() {

        Employee employee = new Employee();
        employee.setId(1L);

        Attendance attendance =
                new Attendance();

        attendance.setId(20L);
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(
                LocalTime.of(9, 0)
        );
        attendance.setCheckOut(
                LocalTime.of(18, 0)
        );
        attendance.setStatus(
                AttendanceStatus.PRESENT
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository.findByEmployeeAndDate(
                employee,
                LocalDate.now()
        )).thenReturn(
                Optional.of(attendance)
        );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> attendanceService.checkOut(1L)
                );

        assertEquals(
                "Employee has already checked out",
                exception.getMessage()
        );

        verify(attendanceRepository, never())
                .save(any(Attendance.class));
    }


    // ==========================================
    // GET EMPLOYEE ATTENDANCE
    // ==========================================

    @Test
    void shouldReturnEmployeeAttendance() {

        Employee employee = new Employee();
        employee.setId(1L);

        Attendance attendance =
                new Attendance();

        attendance.setId(10L);
        attendance.setEmployee(employee);
        attendance.setDate(
                LocalDate.of(2026, 9, 27)
        );
        attendance.setCheckIn(
                LocalTime.of(9, 0)
        );
        attendance.setCheckOut(
                LocalTime.of(18, 0)
        );
        attendance.setStatus(
                AttendanceStatus.PRESENT
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository.findByEmployeeId(1L))
                .thenReturn(List.of(attendance));

        List<AttendanceResponseDto> result =
                attendanceService.getEmployeeAttendance(1L);

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getEmployeeId()
        );

        assertEquals(
                "PRESENT",
                result.get(0).getStatus()
        );

        verify(attendanceRepository)
                .findByEmployeeId(1L);
    }


    // ==========================================
    // MONTHLY ATTENDANCE
    // ==========================================

    @Test
    void shouldReturnMonthlyAttendance() {

        Employee employee = new Employee();
        employee.setId(1L);

        Attendance attendance =
                new Attendance();

        attendance.setId(10L);
        attendance.setEmployee(employee);
        attendance.setDate(
                LocalDate.of(2026, 9, 15)
        );
        attendance.setCheckIn(
                LocalTime.of(9, 0)
        );
        attendance.setCheckOut(
                LocalTime.of(18, 0)
        );
        attendance.setStatus(
                AttendanceStatus.PRESENT
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(attendanceRepository
                .findByEmployeeIdAndDateBetween(
                        eq(1L),
                        eq(LocalDate.of(2026, 9, 1)),
                        eq(LocalDate.of(2026, 9, 30))
                ))
                .thenReturn(List.of(attendance));

        List<AttendanceResponseDto> result =
                attendanceService.getMonthlyAttendance(
                        1L,
                        9,
                        2026
                );

        assertEquals(1, result.size());

        assertEquals(
                LocalDate.of(2026, 9, 15),
                result.get(0).getDate()
        );

        assertEquals(
                "PRESENT",
                result.get(0).getStatus()
        );

        verify(attendanceRepository)
                .findByEmployeeIdAndDateBetween(
                        1L,
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 30)
                );
    }
}