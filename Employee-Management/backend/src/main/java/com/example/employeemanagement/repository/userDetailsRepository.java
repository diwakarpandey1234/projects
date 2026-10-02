package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userDetailsRepository extends JpaRepository<User, Long> {
    Optional<User>findByUserName(String userName);

    List<User> findByRole(Role role);

   // List<User> findByUserName(String userName);


}
