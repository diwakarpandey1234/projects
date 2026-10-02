package com.example.employeemanagement.controller;


import com.example.employeemanagement.config.SecurityConfig;
import com.example.employeemanagement.dto.DashboardResponseDTO;
import com.example.employeemanagement.service.DashboardService;

import org.junit.jupiter.api.Test;
import com.example.employeemanagement.filter.JWTAuthFilter;
import com.example.employeemanagement.jwt.JWTUtil;
import com.example.employeemanagement.service.CustomDetailsUserService;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@Import(SecurityConfig.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomDetailsUserService customDetailsUserService;


    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"DASHBOARD_READ"}
    )
    void adminShouldAccessDashboard() throws Exception {

        DashboardResponseDTO dashboard =
                new DashboardResponseDTO(
                        10L,
                        7L,
                        3L,
                        4L,
                        2L,
                        5L,
                        1L,
                        6L,
                        2L,
                        1L,
                        1L,
                        8L
                );

        when(dashboardService.getDashboard())
                .thenReturn(dashboard);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEmployees").value(10))
                .andExpect(jsonPath("$.activeEmployees").value(7))
                .andExpect(jsonPath("$.inactiveEmployees").value(3))
                .andExpect(jsonPath("$.totalDepartments").value(4))
                .andExpect(jsonPath("$.pendingLeaves").value(2))
                .andExpect(jsonPath("$.approvedLeaves").value(5))
                .andExpect(jsonPath("$.rejectedLeaves").value(1))
                .andExpect(jsonPath("$.todayPresent").value(6))
                .andExpect(jsonPath("$.todayAbsent").value(2))
                .andExpect(jsonPath("$.todayLate").value(1))
                .andExpect(jsonPath("$.todayHalfDay").value(1))
                .andExpect(jsonPath("$.totalSalaryRecords").value(8));
    }


    @Test
    @WithMockUser(
            username = "user",
            authorities = {"EMPLOYEE_READ"}
    )
    void userWithoutDashboardPermissionShouldGetForbidden()
            throws Exception {

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isForbidden());
    }


    @Test
    void unauthenticatedUserShouldGetUnauthorized() throws Exception {

        mockMvc.perform(get("/dashboard"))
                .andDo(print());
    }
}