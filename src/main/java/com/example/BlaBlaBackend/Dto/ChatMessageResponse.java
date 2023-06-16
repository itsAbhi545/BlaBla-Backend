package com.example.BlaBlaBackend.Dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private String roomName;
    private String message;
    private LocalDateTime publishTime;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public ChatMessageResponse roomName(String roomName){
        setRoomName(roomName);
        return this;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public ChatMessageResponse message(String message){
        setMessage(message);
        return this;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }
    public ChatMessageResponse publishTime(LocalDateTime publishTime){
        setPublishTime(publishTime);
        return this;
    }
}
