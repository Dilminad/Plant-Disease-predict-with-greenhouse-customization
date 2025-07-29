package com.example.server.repository.ChatRepo;

import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.model.Chatroom.ChatMessage.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    // Paginated
    Page<ChatMessage> findByRoomIdOrderByTimestampDesc(String roomId, Pageable pageable);
    
    // Non-paginated
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);
    List<ChatMessage> findByRoomIdOrderByTimestampDesc(String roomId);
    
    // Query methods
    List<ChatMessage> findByRoomIdAndSenderIdNotAndStatus(
        String roomId, 
        String senderId, 
        MessageStatus status
    );
    
    long countByRoomIdAndSenderIdNotAndStatus(
        String roomId, 
        String senderId, 
        MessageStatus status
    );
    
    // Basic query
    List<ChatMessage> findByRoomId(String roomId);
}