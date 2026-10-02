package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.AuditResponseDTO;
import com.example.employeemanagement.entity.Audit;
import com.example.employeemanagement.entity.AuditLog;
import com.example.employeemanagement.repository.AuditRepository;
import com.example.employeemanagement.repository.AuditRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditRepository auditLogRepository;

    public AuditService(AuditRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // =========================
    // CREATE AUDIT LOG
    // =========================

    public AuditResponseDTO createAuditLog(
            String username,
            String action,
            String entity,
            Long entityId,
            String details) {

        Audit auditLog = new Audit();

        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setEntity(entity);
        auditLog.setEntityId(entityId);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails(details);

        Audit savedLog =
                auditLogRepository.save(auditLog);

        return convertToDTO(savedLog);
    }




    // GET ALL LOGS


    public List<AuditResponseDTO> getAllLogs() {

        return auditLogRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }





    // GET BY USERNAME

    public List<AuditResponseDTO> getLogsByUsername(
            String username) {

        return auditLogRepository
                .findByUsername(username)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // GET BY ACTION

    public List<AuditResponseDTO> getLogsByAction(
            String action) {

        return auditLogRepository
                .findByAction(action)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // GET BY ENTITY


    public List<AuditResponseDTO> getLogsByEntity(
            String entity) {

        return auditLogRepository
                .findByEntity(entity)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // GET BY ENTITY + ID

    public List<AuditResponseDTO> getLogsByEntityAndId(
            String entity,
            Long entityId) {

        return auditLogRepository
                .findByEntityAndEntityId(entity, entityId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // CONVERT ENTITY → DTO

    private AuditResponseDTO convertToDTO(
            Audit auditLog) {

        return new AuditResponseDTO(
                auditLog.getId(),
                auditLog.getUsername(),
                auditLog.getAction(),
                auditLog.getEntity(),
                auditLog.getEntityId(),
                auditLog.getTimestamp(),
                auditLog.getDetails()
        );
    }
}