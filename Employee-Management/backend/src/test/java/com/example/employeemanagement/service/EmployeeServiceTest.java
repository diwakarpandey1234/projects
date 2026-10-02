package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.entity.Departments;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.userDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private userDetailsRepository userRepository;

    @InjectMocks
    private EmployeeService employeeService;


    @BeforeEach
    void setUp() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "admin",
                        "password",
                        List.of()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }


    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void shouldCreateEmployeeSuccessfully() {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setFirstName("xyz");
        dto.setLastName("uvw");
        dto.setEmail("abc@gmail.com");
        dto.setStatus("ACTIVE");
        dto.setPhone("9876554210");
        dto.setDesignation("Software Developer");
        dto.setJoiningDate("2026-09-27");
        dto.setDepartmentId(1L);
        dto.setUserId(2L);

        Departments department = new Departments();
        department.setId(1L);
        department.setName("IT");

        User user = new User();
        user.setId(2L);

        Employee savedEmployee = new Employee();

        savedEmployee.setId(10L);
        savedEmployee.setFirstName("xyz");
        savedEmployee.setLastName("uvw");
        savedEmployee.setEmail("abc@gmail.com");

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        String result = employeeService.createEmployee(dto);

        assertEquals(
                "Employee Added Successfully",
                result
        );

        verify(departmentRepository)
                .findById(1L);

        verify(userRepository)
                .findById(2L);

        verify(employeeRepository)
                .save(any(Employee.class));

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("EMPLOYEE_CREATED"),
                        eq("Employee"),
                        eq(10L),
                        eq("Employee created: xyz")
                );
    }


    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setFirstName("xyz");
        dto.setDepartmentId(1L);
        dto.setUserId(2L);

        Departments department = new Departments();

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.createEmployee(dto)
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));

        verify(auditService, never())
                .createAuditLog(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString()
                );
    }


    @Test
    void shouldThrowExceptionWhenDepartmentDoesNotExist() {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setFirstName("xyz");
        dto.setDepartmentId(1L);
        dto.setUserId(2L);

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> employeeService.createEmployee(dto)
        );

        verify(userRepository, never())
                .findById(anyLong());

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }


    @Test
    void shouldReturnAllEmployees() {

        Departments department = new Departments();
        department.setName("IT");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");
        employee.setDesignation("Developer");
        employee.setDepartment(department);
        employee.setSalaries(List.of());

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));

        List<EmployeeResponseDTO> result =
                employeeService.getAllEmployees();

        assertEquals(1, result.size());

        EmployeeResponseDTO response = result.get(0);

        assertEquals(1L, response.getId());
        assertEquals("xyz", response.getFirstName());
        assertEquals("uvw", response.getLastName());
        assertEquals("abc@gmail.com", response.getEmail());
        assertEquals("Developer", response.getDesignation());
        assertEquals("IT", response.getDepartmentName());

        verify(employeeRepository)
                .findAll();
    }


    @Test
    void shouldFindEmployeeByEmail() {

        Departments department = new Departments();
        department.setName("IT");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");
        employee.setDesignation("Developer");
        employee.setDepartment(department);
        employee.setSalaries(List.of());

        when(employeeRepository.getByEmail("abc@gmail.com"))
                .thenReturn(employee);

        EmployeeResponseDTO result =
                employeeService.findByEmail("abc@gmail.com");

        assertEquals(1L, result.getId());
        assertEquals("xyz", result.getFirstName());
        assertEquals("uvw", result.getLastName());
        assertEquals("abc@gmail.com", result.getEmail());
        assertEquals("IT", result.getDepartmentName());

        verify(employeeRepository)
                .getByEmail("abc@gmail.com");
    }


    @Test
    void shouldFindEmployeesByFirstName() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setEmail("abc@gmail.com");
        employee.setDesignation("Developer");

        when(employeeRepository.getByFirstName("xtz"))
                .thenReturn(List.of(employee));

        List<EmployeeResponseDTO> result =
                employeeService.findByFirstName("xyz");

        assertEquals(1, result.size());
        assertEquals("xyz", result.get(0).getFirstName());

        verify(employeeRepository)
                .getByFirstName("xyz");
    }


    @Test
    void shouldFindEmployeesByLastName() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");
        employee.setDesignation("Developer");

        when(employeeRepository.getByLastName("uvw"))
                .thenReturn(List.of(employee));

        List<EmployeeResponseDTO> result =
                employeeService.findByLastName("uvw");

        assertEquals(1, result.size());
        assertEquals("uvw", result.get(0).getLastName());

        verify(employeeRepository)
                .getByLastName("uvw");
    }


    @Test
    void shouldDeleteEmployeeSuccessfully() {

        when(employeeRepository.existsById(1L))
                .thenReturn(true);

        String result =
                employeeService.deleteEmployee(1L);

        assertEquals(
                "Employee Deleted With Id ->1",
                result
        );

        verify(employeeRepository)
                .deleteById(1L);

        verify(auditService)
                .createAuditLog(
                        eq("admin"),
                        eq("EMPLOYEE_DELETED"),
                        eq("Employee"),
                        eq(1L),
                        eq("Employee deleted with id: 1")
                );
    }

    @Test
    void shouldReturnMessageWhenEmployeeDoesNotExist() {

        when(employeeRepository.existsById(99L))
                .thenReturn(false);

        String result =
                employeeService.deleteEmployee(99L);

        assertEquals(
                "Provide valid Id",
                result
        );

        verify(employeeRepository, never())
                .deleteById(anyLong());

        verify(auditService, never())
                .createAuditLog(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString()
                );
    }


    @Test
    void shouldReturnPaginatedEmployees() {

        Departments department = new Departments();
        department.setName("IT");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");
        employee.setDesignation("Developer");
        employee.setDepartment(department);
        employee.setSalaries(List.of());

        Page<Employee> page =
                new PageImpl<>(
                        List.of(employee),
                        PageRequest.of(0, 10),
                        1
                );

        when(employeeRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        Page<EmployeeResponseDTO> result =
                employeeService.getEmployees(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(
                "xyz",
                result.getContent().get(0).getFirstName()
        );
        assertEquals(
                "IT",
                result.getContent().get(0).getDepartmentName()
        );
    }
}