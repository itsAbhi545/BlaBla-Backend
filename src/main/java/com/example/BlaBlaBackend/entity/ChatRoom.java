package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="chatroom")
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String chatRoomName;
    @CreationTimestamp
    private LocalDateTime chatRoomCreatedAt;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
    public ChatRoom chatRoomName(String chatRoomName){
        setChatRoomName(chatRoomName);
        return this;
    }
    public LocalDateTime getChatRoomCreatedAt() {
        return chatRoomCreatedAt;
    }
}
