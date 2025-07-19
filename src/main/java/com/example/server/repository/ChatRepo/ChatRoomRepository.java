package com.example.server.repository.ChatRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Chatroom.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findByParticipantIdsContains(String participantId);

    List<ChatRoom> findByType(ChatRoom.ChatRoomType type);

    ChatRoom findByParticipantIdsContainsAndType(String participantId1, String participantId2,
            ChatRoom.ChatRoomType type);
}