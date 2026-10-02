package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.NotificationRequestDTO;
import com.example.employeemanagement.dto.NotificationResponseDTO;
import com.example.employeemanagement.entity.Notification;
import com.example.employeemanagement.entity.NotificationType;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.NotificationRepository;
import com.example.employeemanagement.repository.userDetailsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final userDetailsRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            userDetailsRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }



    public NotificationResponseDTO createNotification(
            NotificationRequestDTO request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved =
                notificationRepository.save(notification);

        return convertToDTO(saved);
    }



    public List<NotificationResponseDTO> getUserNotifications(
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return notificationRepository
                .findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }



    public List<NotificationResponseDTO> getUnreadNotifications(
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return notificationRepository
                .findByUserAndReadFalse(user)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }



    public long getUnreadCount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return notificationRepository
                .countByUserAndReadFalse(user);
    }



    public NotificationResponseDTO markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                ));

        notification.setRead(true);

        Notification updated =
                notificationRepository.save(notification);

        return convertToDTO(updated);
    }




    public String markAllAsRead(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        List<Notification> notifications =
                notificationRepository.findByUserAndReadFalse(user);

        notifications.forEach(notification ->
                notification.setRead(true)
        );

        notificationRepository.saveAll(notifications);

        return "All notifications marked as read";
    }



    public String deleteNotification(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                ));

        notificationRepository.delete(notification);

        return "Notification deleted";
    }



    private NotificationResponseDTO convertToDTO(
            Notification notification) {

        return new NotificationResponseDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }




    public void sendNotification(
            Long userId,
            String title,
            String message,
            NotificationType type) {

        NotificationRequestDTO request =
                new NotificationRequestDTO();

        request.setUserId(userId);
        request.setTitle(title);
        request.setMessage(message);
        request.setType(type);

        createNotification(request);
    }
}