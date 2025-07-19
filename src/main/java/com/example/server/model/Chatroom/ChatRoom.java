package com.example.server.model.Chatroom;

import java.time.LocalDateTime;
import java.util.Set;

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
@Document(collection = "chat_rooms")
public class ChatRoom {
    @Id
    private String id;
    private Set<String> participantIds;
    private String name;
    private ChatRoomType type;
    private LocalDateTime createdAt;
    private String lastMessagePreview;
    private LocalDateTime lastActivity;
    
    public enum ChatRoomType { PRIVATE, GROUP, SUPPORT }

    
}