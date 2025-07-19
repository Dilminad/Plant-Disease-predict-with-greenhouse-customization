package com.example.server.service.ChatServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.server.model.Chatroom.ChatRoom;
import com.example.server.model.Chatroom.ChatRoom.ChatRoomType;
import com.example.server.repository.ChatRepo.ChatRoomRepository;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom createPrivateChatRoom(String participantId1, String participantId2) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setParticipantIds(Set.of(participantId1, participantId2));
        chatRoom.setType(ChatRoomType.PRIVATE);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getChatRoomsByParticipant(String participantId) {
        return chatRoomRepository.findByParticipantIdsContains(participantId);
    }

    public List<ChatRoom> getChatRoomsByType(ChatRoomType type) {
        return chatRoomRepository.findByType(type);
    }

    public ChatRoom getPrivateChatRoom(String participantId1, String participantId2) {
        return chatRoomRepository.findByParticipantIdsContainsAndType(participantId1, participantId2, ChatRoomType.PRIVATE);
    }

    public ChatRoom updateChatRoom(ChatRoom chatRoom) {
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    public void updateLastMessagePreview(String roomId, String messagePreview) {
        chatRoomRepository.findById(roomId)
                .map(room -> {
                    room.setLastMessagePreview(messagePreview);
                    room.setLastActivity(LocalDateTime.now());
                    return chatRoomRepository.save(room);
                })
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + roomId));
    }

    public void deleteChatRoom(String roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}