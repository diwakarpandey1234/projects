package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.*;
import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.repository.userDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.employeemanagement.entity.Employee;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class UserService {

    private final userDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            userDetailsRepository userDetailsRepository,
            PasswordEncoder passwordEncoder) {

        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterUserRequest request) {

        if (userDetailsRepository
                .findByUserName(request.getUsername())
                .isPresent()) {

            throw new RuntimeException("User already exists");
        }

        User user = new User();

        user.setUserName(request.getUsername());

        // IMPORTANT:
        // User gets USER role automatically
        user.setRole(Role.USER);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );


        User savedUser = userDetailsRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getUserID());
        response.setUsername(savedUser.getUsername());
        response.setRole(String.valueOf(savedUser.getRole()));

        return response;
    }



    public CurrentUserResponseDTO getCurrentUser(String   username) {

        User user = userDetailsRepository
                .findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CurrentUserResponseDTO response = new CurrentUserResponseDTO();

        response.setUserId(user.getUserID());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());

        Employee employee = user.getEmployee();

        if (employee != null) {
            response.setEmployeeId(employee.getEmployeeID());
            response.setFirstName(employee.getFirstName());
            response.setLastName(employee.getLastName());
            response.setEmail(employee.getEmail());
            response.setPhone(employee.getPhone());
            response.setDesignation(employee.getDesignation());
            response.setStatus(employee.getStatus());
        }

        return response;
    }



    public List<EmployeeResponseDTO> getHRUsers() {

        return userDetailsRepository.findByRole(Role.HR)
                .stream()
                .map(user -> { Employee employee = user.getEmployee();

        // Map Salary entities into SalaryResponseDTO
                List<SalaryResponseDTO> salaryDTOs = employee.getSalaries()
                .stream()
                .map(salary -> new SalaryResponseDTO(
                        salary.getId(),
                        salary.getNetSalary()
                ))
                .toList();

        return new EmployeeResponseDTO(
                employee.getEmployeeID(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDesignation(),
                employee.getDepartment().getName(),
                salaryDTOs
        );
    }).toList();
    }
}