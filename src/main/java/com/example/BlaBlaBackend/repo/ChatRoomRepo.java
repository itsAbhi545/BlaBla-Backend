package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepo extends JpaRepository<ChatRoom,Integer> {
    ChatRoom findChatRoomByChatRoomName(String chatRoomName);
}
