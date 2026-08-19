package com.example.backend.dto.notification;
import com.example.backend.entity.Notification; import com.example.backend.entity.NotificationType; import java.time.LocalDateTime;
public record NotificationResponse(Long id, NotificationType type, Long resourceId, String message, LocalDateTime readAt, LocalDateTime createdAt, Long actorId) { public static NotificationResponse from(Notification n){return new NotificationResponse(n.getId(),n.getType(),n.getResourceId(),n.getMessage(),n.getReadAt(),n.getCreatedAt(),n.getActor()==null?null:n.getActor().getId());} }
