package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.AuditResponseDTO;
import com.example.employeemanagement.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditLogController {

    private final AuditService auditLogService;

    public AuditLogController(AuditService auditLogService) {
        this.auditLogService = auditLogService;
    }



    // =========================
    // GET ALL AUDIT LOGS
    // =========================

    @GetMapping
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<List<AuditResponseDTO>> getAllLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllLogs()
        );
    }

    // =========================
    // GET LOGS BY USER
    // =========================



    @GetMapping("/user/{username}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<List<AuditResponseDTO>> getLogsByUsername(
            @PathVariable String username) {

        return ResponseEntity.ok(
                auditLogService.getLogsByUsername(username)
        );
    }



    // =========================
    // GET LOGS BY ACTION
    // =========================

    @GetMapping("/action/{action}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<List<AuditResponseDTO>> getLogsByAction(
            @PathVariable String action) {

        return ResponseEntity.ok(
                auditLogService.getLogsByAction(action)
        );
    }



    // =========================
    // GET LOGS BY ENTITY
    // =========================

    @GetMapping("/entity/{entity}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<List<AuditResponseDTO>> getLogsByEntity(
            @PathVariable String entity) {

        return ResponseEntity.ok(
                auditLogService.getLogsByEntity(entity)
        );
    }





    // =========================
    // GET LOGS BY ENTITY + ID
    // =========================

    @GetMapping("/entity/{entity}/{entityId}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<List<AuditResponseDTO>> getLogsByEntityAndId(
            @PathVariable String entity,
            @PathVariable Long entityId) {

        return ResponseEntity.ok(
                auditLogService.getLogsByEntityAndId(
                        entity,
                        entityId
                )
        );
    }
}