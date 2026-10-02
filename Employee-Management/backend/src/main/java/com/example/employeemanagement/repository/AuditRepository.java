package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Audit;
import com.example.employeemanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findByUsername(String username);

    List<Audit> findByAction(String action);

    List<Audit> findByEntity(String entity);

    List<Audit> findByEntityAndEntityId(
            String entity,
            Long entityId
    );
}