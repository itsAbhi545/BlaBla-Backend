package com.example.BlaBlaBackend.Dto;

import java.time.LocalDateTime;

public class ChatResponse {
    private int sender_id;
    private int recipient_id;
    private int message_id;
    private String message;
    private LocalDateTime message_send_at;

    public ChatResponse(int sender_id, int recipient_id, int message_id, String message, LocalDateTime message_send_at) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.message_id = message_id;
        this.message = message;
        this.message_send_at = message_send_at;
    }

    public int getSender_id() {
        return sender_id;
    }

    public int getRecipient_id() {
        return recipient_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getMessage_send_at() {
        return message_send_at;
    }
}
