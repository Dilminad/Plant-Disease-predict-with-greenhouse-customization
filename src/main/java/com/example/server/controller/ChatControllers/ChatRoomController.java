package com.example.server.controller.ChatControllers;

import com.example.server.model.Chatroom.ChatRoom;
import com.example.server.service.ChatServices.ChatRoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController

public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/auth/privatechat")
    public ResponseEntity<ChatRoom> createPrivateChat(
            @RequestBody Set<String> participantIds) {
        return ResponseEntity.ok(chatRoomService.createPrivateChatRoom(participantIds));
    }

    @PostMapping("/group")
    public ResponseEntity<ChatRoom> createGroupChat(
            @RequestParam Set<String> participantIds,
            @RequestParam String name) {
        return ResponseEntity.ok(chatRoomService.createGroupChatRoom(participantIds, name));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable String roomId) {
        ChatRoom room = chatRoomService.getChatRoomById(roomId);
        return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoom>> getUserChatRooms(@PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsForUser(userId));
    }

    @GetMapping("/private/{userId1}/{userId2}")
    public ResponseEntity<ChatRoom> getPrivateChatBetweenUsers(
            @PathVariable String userId1,
            @PathVariable String userId2) {
        ChatRoom room = chatRoomService.findPrivateChatBetweenUsers(userId1, userId2);
        return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}