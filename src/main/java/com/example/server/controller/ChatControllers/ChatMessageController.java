package com.example.server.controller.ChatControllers;

import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.service.ChatServices.ChatMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<ChatMessage> sendMessage(
            @RequestParam String roomId,
            @RequestParam String senderId,
            @RequestParam String content,
            @RequestParam(required = false) List<String> attachments) {
        return ResponseEntity.ok(
                chatMessageService.sendMessage(roomId, senderId, content, attachments));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatMessage>> getRoomMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(chatMessageService.getMessagesForRoom(roomId));
    }

    @GetMapping("/unread/{roomId}/{userId}")
    public ResponseEntity<Long> countUnreadMessages(
            @PathVariable String roomId,
            @PathVariable String userId) {
        return ResponseEntity.ok(
                chatMessageService.countUnreadMessages(roomId, userId));
    }

    @PutMapping("/read/{roomId}/{userId}")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable String roomId,
            @PathVariable String userId) {
        chatMessageService.markMessagesAsRead(roomId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        chatMessageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}