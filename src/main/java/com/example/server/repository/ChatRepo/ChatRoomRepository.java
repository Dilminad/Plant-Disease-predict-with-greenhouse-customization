package com.example.server.repository.ChatRepo;

import com.example.server.model.Chatroom.ChatRoom;
import com.example.server.model.Chatroom.ChatRoom.ChatRoomType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    // Find exact private chat between participants
    Optional<ChatRoom> findByParticipantIdsAndType(Set<String> participantIds, ChatRoomType type);
    
    // Find rooms containing all specified participants
    List<ChatRoom> findByParticipantIdsContaining(Set<String> participantIds);
    
    // Find rooms where user is a participant
    List<ChatRoom> findByParticipantIdsContains(String userId);
    
    // Find rooms by type
    List<ChatRoom> findByType(ChatRoomType type);
}