package com.example.server.service.ChatServices;

import com.example.server.exception.MessageNotFoundException;
import com.example.server.model.Chatroom.ChatMessage;
import com.example.server.model.Chatroom.ChatMessage.MessageStatus;
import com.example.server.repository.ChatRepo.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Transactional
    public ChatMessage saveMessage(String roomId, String senderId, String content) {
        chatRoomService.getRoomById(roomId);
        
        ChatMessage message = new ChatMessage();
        message.setRoomId(roomId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        
        chatRoomService.updateLastActivity(roomId);
        return chatMessageRepository.save(message);
    }

    // Main paginated method
    public Page<ChatMessage> getRoomMessages(String roomId, Pageable pageable) {
        return chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId, pageable);
    }

    // Alternative limit/offset version
    public List<ChatMessage> getRoomMessagesWithLimit(String roomId, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        return getRoomMessages(roomId, pageable).getContent();
    }

    // Get all messages (no pagination)
    public List<ChatMessage> getAllRoomMessages(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    @Transactional
    public void markMessagesAsRead(String roomId, String userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findByRoomIdAndSenderIdNotAndStatus(roomId, userId, MessageStatus.SENT);
        unreadMessages.forEach(msg -> msg.setStatus(MessageStatus.READ));
        chatMessageRepository.saveAll(unreadMessages);
    }

    public ChatMessage getMessageById(String messageId) {
        return chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
    }

    @Transactional
    public ChatMessage editMessage(String messageId, String newContent, String userId) {
        ChatMessage message = getMessageById(messageId);
        if (!message.getSenderId().equals(userId)) {
            throw new SecurityException("Only the message sender can edit this message");
        }
        message.setContent(newContent);
        message.setEdited(true);
        return chatMessageRepository.save(message);
    }

    @Transactional
    public void deleteMessage(String messageId, String userId) {
        ChatMessage message = getMessageById(messageId);
        if (!message.getSenderId().equals(userId)) {
            throw new SecurityException("Only the message sender can delete this message");
        }
        message.setDeleted(true);
        chatMessageRepository.save(message);
    }

    public long countUnreadMessages(String roomId, String userId) {
        return chatMessageRepository.countByRoomIdAndSenderIdNotAndStatus(
            roomId, userId, MessageStatus.READ);
    }
}