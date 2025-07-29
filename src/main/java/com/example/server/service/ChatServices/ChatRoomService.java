package com.example.server.service.ChatServices;

import com.example.server.exception.ChatRoomNotFoundException;
import com.example.server.exception.InvalidChatPermissionException;
import com.example.server.model.Chatroom.ChatRoom;
import com.example.server.model.Chatroom.ChatRoom.ChatRoomType;
import com.example.server.repository.ChatRepo.ChatRoomRepository;
import com.example.server.repository.RolesRepo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private FarmerRepository farmerRepository;
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Autowired
    private SeedSellerRepository seedSellerRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    private static final Map<String, Set<String>> ALLOWED_CHATS = Map.of(
        "ROLE_FARMER", Set.of("ROLE_FARMER", "ROLE_BUYER", "ROLE_SEED_SELLER"),
        "ROLE_BUYER", Set.of("ROLE_FARMER"),
        "ROLE_SEED_SELLER", Set.of("ROLE_FARMER"),
        "ROLE_ADMIN", Set.of("ROLE_FARMER", "ROLE_BUYER", "ROLE_SEED_SELLER", "ROLE_ADMIN")
    );

    public ChatRoom getOrCreatePrivateRoom(String user1, String user1Role, String user2) {
        String user2Role = getUserRole(user2);
        validateChatPermission(user1Role, user2Role);

        Set<String> participants = Set.of(user1, user2);
        return chatRoomRepository.findByParticipantIdsAndType(participants, ChatRoomType.PRIVATE)
            .orElseGet(() -> createNewPrivateRoom(participants));
    }

    private ChatRoom createNewPrivateRoom(Set<String> participants) {
        ChatRoom newRoom = new ChatRoom();
        newRoom.setParticipantIds(participants);
        newRoom.setType(ChatRoomType.PRIVATE);
        newRoom.setCreatedAt(LocalDateTime.now());
        newRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(newRoom);
    }

    public List<ChatRoom> getUserChatRooms(String userId, String userRole) {
        List<ChatRoom> allRooms = chatRoomRepository.findByParticipantIdsContains(userId);
        
        return allRooms.stream()
                .filter(room -> filterAllowedRooms(room, userId, userRole))
                .collect(Collectors.toList());
    }

    private boolean filterAllowedRooms(ChatRoom room, String userId, String userRole) {
        if (room.getType() == ChatRoomType.PRIVATE && room.getParticipantIds().size() == 2) {
            String otherUserId = room.getParticipantIds().stream()
                    .filter(id -> !id.equals(userId))
                    .findFirst()
                    .orElse(null);
            if (otherUserId != null) {
                String otherUserRole = getUserRole(otherUserId);
                return ALLOWED_CHATS.getOrDefault(userRole, Set.of()).contains(otherUserRole);
            }
        }
        return true; // Group chats are allowed for all
    }

    public ChatRoom getRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found with id: " + roomId));
    }

    public ChatRoom createGroupRoom(Set<String> participantIds, String requesterId, String requesterRole) {
        validateGroupParticipants(participantIds, requesterId, requesterRole);
        
        ChatRoom newRoom = new ChatRoom();
        newRoom.setParticipantIds(participantIds);
        newRoom.setType(ChatRoomType.GROUP);
        newRoom.setName(generateDefaultGroupName(participantIds));
        newRoom.setCreatedAt(LocalDateTime.now());
        newRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(newRoom);
    }

    public ChatRoom updateLastActivity(String roomId) {
        ChatRoom room = getRoomById(roomId);
        room.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(room);
    }

    public boolean hasAccess(String roomId, String userId) {
        try {
            ChatRoom room = getRoomById(roomId);
            return room.getParticipantIds().contains(userId);
        } catch (ChatRoomNotFoundException e) {
            return false;
        }
    }

    private void validateChatPermission(String role1, String role2) {
        if (!ALLOWED_CHATS.getOrDefault(role1, Set.of()).contains(role2)) {
            throw new InvalidChatPermissionException(role1 + " is not allowed to chat with " + role2);
        }
    }

    private void validateGroupParticipants(Set<String> participantIds, String requesterId, String requesterRole) {
        if (!participantIds.contains(requesterId)) {
            throw new SecurityException("Requester must be included in the group");
        }
        
        String requesterCleanRole = requesterRole.replace("ROLE_", "");
        if (requesterCleanRole.equals("BUYER") || requesterCleanRole.equals("SEED_SELLER")) {
            throw new SecurityException(requesterCleanRole + " cannot create group chats");
        }
    }

    private String getUserRole(String userId) {
        if (farmerRepository.existsById(userId)) return "ROLE_FARMER";
        if (buyerRepository.existsById(userId)) return "ROLE_BUYER";
        if (seedSellerRepository.existsById(userId)) return "ROLE_SEED_SELLER";
        if (adminRepository.existsById(userId)) return "ROLE_ADMIN";
        throw new SecurityException("User not found");
    }

    private String generateDefaultGroupName(Set<String> participantIds) {
        return "Group: " + participantIds.stream()
                 .limit(3)
                 .map(this::getUsername)
                 .collect(Collectors.joining(", ")) + 
                 (participantIds.size() > 3 ? "..." : "");
    }

    private String getUsername(String userId) {
        // Implement based on your user repositories
        return userId; // Simplified
    }
}