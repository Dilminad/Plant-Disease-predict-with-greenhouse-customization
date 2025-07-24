package com.example.server.service.ChatServices;

import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.model.Chatroom.ChatMessage.MessageStatus;
import com.example.server.repository.ChatRepo.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, 
                            ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    public ChatMessage sendMessage(String roomId, String senderId, String content, 
                                 List<String> attachments) {
        ChatMessage message = new ChatMessage();
        message.setRoomId(roomId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        message.setAttachments(attachments);

        ChatMessage savedMessage = chatMessageRepository.save(message);
        
        // Update chat room last activity
        String preview = content.length() > 30 ? content.substring(0, 30) + "..." : content;
        chatRoomService.updateLastActivity(roomId, preview);
        
        return savedMessage;
    }

    public List<ChatMessage> getMessagesForRoom(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    public long countUnreadMessages(String roomId, String userId) {
        return chatMessageRepository.countByRoomIdAndStatus(
                roomId, MessageStatus.DELIVERED);
    }

    public void markMessagesAsRead(String roomId, String userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findBySenderIdAndStatus(
                userId, MessageStatus.DELIVERED);
        
        unreadMessages.forEach(message -> {
            message.setStatus(MessageStatus.READ);
            chatMessageRepository.save(message);
        });
    }

    public void deleteMessage(String messageId) {
        chatMessageRepository.deleteById(messageId);
    }
}