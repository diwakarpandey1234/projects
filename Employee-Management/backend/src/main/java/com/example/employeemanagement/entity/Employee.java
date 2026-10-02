package com.example.employeemanagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employeeID;

    @NonNull
    @ManyToOne
    private Departments department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Salary> salaries;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Attendance> attendances;


    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private User user;



    @NotBlank
    private String firstName;

    private String lastName;


    @Email
    private String email;

    private String status;
    private String phone;

    private String designation;

    @NotBlank
    private String joiningDate;



}
