package com.example.BlaBlaBackend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

//    @MessageMapping("/chat")
//    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) throws Exception {
//        String sender = sha.getUser().getName();
//        ChatMessage message = new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), chatMessage.getRecipient());
//        if (!sender.equals(chatMessage.getRecipient())) {
//            simpMessagingTemplate.convertAndSendToUser(sender, "/queue/messages", message);
//        }
//
//        simpMessagingTemplate.convertAndSendToUser(chatMessage.getRecipient(), "/queue/messages", message);
//    }
}
