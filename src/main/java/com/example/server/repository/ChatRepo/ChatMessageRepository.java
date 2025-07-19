package com.example.server.repository.ChatRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Chatroom.ChatMessage;

public interface ChatMessageRepository extends MongoRepository <ChatMessage, String>{
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);
    List<ChatMessage> findBySenderIdAndStatus(String senderId, ChatMessage.MessageStatus status);
    long countByRoomIdAndStatus(String roomId, ChatMessage.MessageStatus status);
}


