package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.NotificationRequestDTO;
import com.example.employeemanagement.dto.NotificationResponseDTO;
import com.example.employeemanagement.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ADMIN / HR can create notifications
    @PostMapping
    @PreAuthorize("hasAuthority('NOTIFICATION_WRITE')")
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationService.createNotification(request));
    }



    // Get all notifications of a user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('NOTIFICATION_READ')")
    public ResponseEntity<List<NotificationResponseDTO>>
    getUserNotifications(@PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId)
        );
    }



    // Get unread notifications
    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasAuthority('NOTIFICATION_READ')")
    public ResponseEntity<List<NotificationResponseDTO>>
    getUnreadNotifications(@PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId)
        );
    }



    // Get unread count
    @GetMapping("/user/{userId}/unread/count")
    @PreAuthorize("hasAuthority('NOTIFICATION_READ')")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId)
        );
    }



    // Mark one notification as read
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAuthority('NOTIFICATION_WRITE')")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }



    // Mark all notifications as read
    @PutMapping("/user/{userId}/read-all")
    @PreAuthorize("hasAuthority('NOTIFICATION_WRITE')")
    public ResponseEntity<String> markAllAsRead(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.markAllAsRead(userId)
        );
    }



    // Delete notification
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('NOTIFICATION_WRITE')")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService.deleteNotification(id)
        );
    }
}