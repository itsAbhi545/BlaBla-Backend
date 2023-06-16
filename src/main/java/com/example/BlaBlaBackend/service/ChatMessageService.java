package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Dto.ChatResponse;
import com.example.BlaBlaBackend.entity.ChatMessage;
import com.example.BlaBlaBackend.entity.ChatRoom;
import com.example.BlaBlaBackend.repo.ChatMessageRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepo chatMessageRepo;

    public ChatMessageService(ChatMessageRepo chatMessageRepo) {
        this.chatMessageRepo = chatMessageRepo;
    }
    public ChatMessage saveChatMessageInDb(ChatMessage chatMessage){
        return chatMessageRepo.save(chatMessage);
    }
    public List<ChatResponse> findChatMessages(int roomId){
        return chatMessageRepo.findChatMessages(roomId);
    }
}
