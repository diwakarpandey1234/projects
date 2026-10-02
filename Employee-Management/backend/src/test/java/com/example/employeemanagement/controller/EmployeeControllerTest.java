package com.example.employeemanagement.controller;

import com.example.employeemanagement.config.SecurityConfig;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.jwt.JWTUtil;
import com.example.employeemanagement.service.CustomDetailsUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomDetailsUserService customDetailsUserService;

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"EMPLOYEE_READ"}
    )
    void shouldGetAllEmployees() throws Exception {

        EmployeeResponseDTO employee = new EmployeeResponseDTO();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");
        employee.setDesignation("Developer");
        employee.setDepartmentName("IT");

        when(employeeService.getAllEmployees())
                .thenReturn(List.of(employee));

        mockMvc.perform(get("/Employee/get/Allemployees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("xyz"))
                .andExpect(jsonPath("$[0].lastName").value("uvw"))
                .andExpect(jsonPath("$[0].email")
                        .value("abc@gmail.com"))
                .andExpect(jsonPath("$[0].designation")
                        .value("Developer"))
                .andExpect(jsonPath("$[0].departmentName")
                        .value("IT"));
    }

    @Test
    @WithMockUser(
            username = "user",
            authorities = {"EMPLOYEE_READ"}
    )
    void shouldReturnEmployeesForUserWithReadPermission()
            throws Exception {

        when(employeeService.getAllEmployees())
                .thenReturn(List.of());

        mockMvc.perform(get("/Employee/get/Allemployees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(
            username = "user",
            authorities = {"DEPARTMENT_READ"}
    )
    void shouldReturnForbiddenWithoutEmployeeReadPermission()
            throws Exception {

        mockMvc.perform(get("/Employee/get/Allemployees"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/Employee/get/Allemployees"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"EMPLOYEE_READ"})
    void shouldFindEmployeeByEmail() throws Exception {

        EmployeeResponseDTO employee = new EmployeeResponseDTO();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");

        when(employeeService.findByEmail("abc@gmail.com"))
                .thenReturn(employee);

        mockMvc.perform(
                        get("/Employee/get/ByEmail")
                                .param("emailParam", "abc@gmail.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("xyz"))
                .andExpect(jsonPath("$.lastName").value("uvw"))
                .andExpect(jsonPath("$.email").value("abc@gmail.com"));

        verify(employeeService).findByEmail("abc@gmail.com");
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"EMPLOYEE_READ"})
    void shouldFindEmployeeByEmailUsingPathVariable() throws Exception {

        EmployeeResponseDTO employee = new EmployeeResponseDTO();

        employee.setId(1L);
        employee.setFirstName("xyz");
        employee.setLastName("uvw");
        employee.setEmail("abc@gmail.com");

        when(employeeService.findByEmail("abc@gmail.com"))
                .thenReturn(employee);

        mockMvc.perform(
                        get("/Employee/get/ByEmail/abc@gmail.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abc@gmail.com"));

        verify(employeeService).findByEmail("abc@gmail.com");
    }

//    @Test
//    @WithMockUser(username = "admin", authorities = {"EMPLOYEE_READ"})
//    void shouldFindEmployeeByFirstName() throws Exception {
//
//        EmployeeResponseDTO employee = new EmployeeResponseDTO();
//
//        employee.setId(1L);
//        employee.setFirstName("xyz");
//        employee.setLastName("uvw");
//        employee.setEmail("abc@gmail.com");
//
//        when(employeeService.findByFirstName("xyz"))
//                .thenReturn(List.of(employee));
//
//        mockMvc.perform(
//                        get("/Employee/get/ByFirstName")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("\"xyz\"")
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].firstName").value("xyz"))
//                .andExpect(jsonPath("$[0].lastName").value("uvw"))
//                .andExpect(jsonPath("$[0].email").value("abc@gmail.com"));
//
//        verify(employeeService).findByFirstName("xyz");
//    }
}