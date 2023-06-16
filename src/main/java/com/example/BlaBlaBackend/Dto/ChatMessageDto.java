package com.example.BlaBlaBackend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChatMessageDto {
    @NotBlank(message = "can't be null")
    private String content;
    @NotNull(message = "senderId can't be null")
    private int senderId;
    @NotNull(message = "recipientId can't be null")
    private int recipientId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }
}
