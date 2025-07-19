package com.example.server.service.ChatServices;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.model.Chatroom.ChatMessage.MessageStatus;
import com.example.server.repository.ChatRepo.ChatMessageRepository;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        if (message.getStatus() == null) {
            message.setStatus(MessageStatus.SENT);
        }
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    public List<ChatMessage> getMessagesBySenderAndStatus(String senderId, MessageStatus status) {
        return chatMessageRepository.findBySenderIdAndStatus(senderId, status);
    }

    public long getMessageCountByRoomAndStatus(String roomId, MessageStatus status) {
        return chatMessageRepository.countByRoomIdAndStatus(roomId, status);
    }

    public ChatMessage updateMessageStatus(String messageId, MessageStatus status) {
        return chatMessageRepository.findById(messageId)
                .map(message -> {
                    message.setStatus(status);
                    return chatMessageRepository.save(message);
                })
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
    }

    public void deleteMessage(String messageId) {
        chatMessageRepository.deleteById(messageId);
    }
}