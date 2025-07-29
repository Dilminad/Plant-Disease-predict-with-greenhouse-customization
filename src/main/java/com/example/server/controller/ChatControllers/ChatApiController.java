package com.example.server.controller.ChatControllers;

import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.model.Chatroom.ChatRoom;
import com.example.server.service.ChatServices.ChatMessageService;
import com.example.server.service.ChatServices.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatApiController {

    @Autowired
    private ChatRoomService chatRoomService;
    
    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('FARMER', 'BUYER', 'SEED_SELLER', 'ADMIN')")
    public ResponseEntity<List<ChatRoom>> getUserRooms(Authentication auth) {
        String userId = auth.getName();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(chatRoomService.getUserChatRooms(userId, userRole));
    }

    @GetMapping("/rooms/{roomId}/messages")
    @PreAuthorize("@chatRoomService.hasAccess(#roomId, authentication.name)")
    public ResponseEntity<Page<ChatMessage>> getRoomMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication auth) {
        
        return ResponseEntity.ok(
            chatMessageService.getRoomMessages(roomId, PageRequest.of(page, size))
        );
    }

    @PostMapping("/rooms/private")
    @PreAuthorize("hasAnyRole('FARMER', 'BUYER', 'SEED_SELLER', 'ADMIN')")
    public ResponseEntity<ChatRoom> createPrivateRoom(
            @RequestBody String otherUserId,
            Authentication auth) {
        
        String userId = auth.getName();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();
        
        return ResponseEntity.ok(
            chatRoomService.getOrCreatePrivateRoom(userId, userRole, otherUserId)
        );
    }

    @PostMapping("/rooms/group")
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ResponseEntity<ChatRoom> createGroupRoom(
            @RequestBody Set<String> participantIds,
            Authentication auth) {
        
        String userId = auth.getName();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();
        
        return ResponseEntity.ok(
            chatRoomService.createGroupRoom(participantIds, userId, userRole)
        );
    }

    @GetMapping("/rooms/{roomId}/access")
    public ResponseEntity<Boolean> hasAccess(
            @PathVariable String roomId,
            Authentication auth) {
        return ResponseEntity.ok(
            chatRoomService.hasAccess(roomId, auth.getName())
        );
    }
}