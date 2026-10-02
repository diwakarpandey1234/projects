package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Departments;
import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {


     Employee getByEmail(String email);
     List<Employee> getByFirstName(String firstName);
     List<Employee> getByLastName(String lastName);

     long countByStatus(String status);

     Optional<Employee> findByUserUserName(String username);


     //  Employee findByEmail(String email);
}
