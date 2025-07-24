package com.example.server.service.ChatServices;

import com.example.server.model.Chatroom.ChatRoom;
import com.example.server.model.Chatroom.ChatRoom.ChatRoomType;
import com.example.server.repository.ChatRepo.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createPrivateChatRoom(Set<String> participantIds) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setParticipantIds(participantIds);
        chatRoom.setType(ChatRoomType.PRIVATE);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom createGroupChatRoom(Set<String> participantIds, String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setParticipantIds(participantIds);
        chatRoom.setType(ChatRoomType.GROUP);
        chatRoom.setName(name);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoomById(String roomId) {
        return chatRoomRepository.findById(roomId).orElse(null);
    }

    public List<ChatRoom> getChatRoomsForUser(String userId) {
        return chatRoomRepository.findByParticipantIdsContains(userId);
    }

    public ChatRoom findPrivateChatBetweenUsers(String userId1, String userId2) {
        return chatRoomRepository.findByParticipantIdsContainsAndType(
                userId1, userId2, ChatRoomType.PRIVATE);
    }

    public ChatRoom updateLastActivity(String roomId, String lastMessagePreview) {
        ChatRoom chatRoom = getChatRoomById(roomId);
        if (chatRoom != null) {
            chatRoom.setLastActivity(LocalDateTime.now());
            chatRoom.setLastMessagePreview(lastMessagePreview);
            return chatRoomRepository.save(chatRoom);
        }
        return null;
    }

    public void deleteChatRoom(String roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}