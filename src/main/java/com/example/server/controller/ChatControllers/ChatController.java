package com.example.server.controller.ChatControllers;

import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.service.ChatServices.ChatMessageService;
import com.example.server.service.ChatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ChatMessageService chatMessageService;
    
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/chat/{roomId}/send")
    @PreAuthorize("@chatRoomService.hasAccess(#roomId, authentication.name)")
    public void sendMessage(
            @DestinationVariable String roomId,
            @Payload ChatMessage message,
            Authentication auth) {
        
        // Basic XSS protection
        message.setContent(sanitize(message.getContent()));
        
        // Save and broadcast
        ChatMessage saved = chatMessageService.saveMessage(
            roomId,
            auth.getName(),
            message.getContent()
        );
        
        messagingTemplate.convertAndSend("/topic/room/" + roomId, saved);
        
        // Update last activity
        chatRoomService.updateLastActivity(roomId);
    }

    private String sanitize(String input) {
        // Basic HTML escaping
        return input.replace("<", "&lt;").replace(">", "&gt;");
    }
}