package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.ChatRoom;
import com.example.BlaBlaBackend.repo.ChatRoomRepo;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {
    private final ChatRoomRepo chatRoomRepo;
    public ChatRoomService(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }
    public ChatRoom findChatRoomByChatRoomName(String chatRoomName){
        return chatRoomRepo.findChatRoomByChatRoomName(chatRoomName);
    }
    public ChatRoom createChatRoom(ChatRoom chatRoom){
        return chatRoomRepo.save(chatRoom);
    }
}
