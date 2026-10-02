package com.example.employeemanagement.repository;


import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary,Long> {

    //Employee getByEmail(String email);

    //List<Employee> getByFirstName(String firstName);

    //List<Employee> getByLastName(String lastName);

    //long countByStatus(String status);

    //Optional<Employee> findByUserUserName(String userName);

    List<Salary> findByEmployeeId(Long id);
}
