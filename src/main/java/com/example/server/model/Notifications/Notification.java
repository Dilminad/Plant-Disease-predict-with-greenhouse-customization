package com.example.server.model.Notifications;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
     @Id
    private String id;
    private String userId;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private boolean read = false;
    private NotificationType type;
    private String relatedEntityId; // ID of order, message, etc.
    
    public enum NotificationType {
        ORDER_UPDATE, SYSTEM_ALERT, CHAT_MESSAGE, GREENHOUSE_ALERT
    }
}

