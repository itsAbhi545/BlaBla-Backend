package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.Dto.ChatResponse;
import com.example.BlaBlaBackend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessage,Integer> {
    @Query("Select new com.example.BlaBlaBackend.Dto.ChatResponse(cm.sender.id,cm.recipient.id,cm.messageId,cm.message,cm.messageSendAt) from ChatMessage cm where cm.chatRoom.roomId = ?1")
    List<ChatResponse> findChatMessages(int roomId);
}
