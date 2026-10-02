package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Leave;
import com.example.employeemanagement.entity.LeaveStatus;
import com.example.employeemanagement.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByEmployeeId(Long employeeId);

    List<Leave> findByEmployeeIdAndStatus(
            Long employeeId,
            LeaveStatus status
    );

    List<Leave> findByStatus(LeaveStatus status);

    List<Leave> findByLeaveType(LeaveType leaveType);

    List<Leave> findByStartDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    long countByStatus(LeaveStatus status);
}