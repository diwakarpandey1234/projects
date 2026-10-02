package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.CurrentUserResponseDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.dto.RegisterUserRequest;
import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.dto.UserResponse;
import com.example.employeemanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody RegisterUserRequest request) {

        UserResponse response =
                userService.registerUser(request);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDTO> getCurrentUser(
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                userService.getCurrentUser(username)
        );
    }


    @GetMapping("/hr")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<List<EmployeeResponseDTO>> getHRUsers() {
        return ResponseEntity.ok(userService.getHRUsers());
    }
}