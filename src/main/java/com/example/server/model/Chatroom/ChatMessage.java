package com.example.server.model.Chatroom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
    private MessageStatus status;
    private List<String> attachments;
     private boolean edited;      // New field to track edits
    private boolean deleted; 
    
    public enum MessageStatus { SENT, DELIVERED, READ }

}