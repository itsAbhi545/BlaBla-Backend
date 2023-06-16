package com.example.BlaBlaBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="chatmessage")
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;
    @NotNull(message = "Message can't be null")
    private String message;
    @CreationTimestamp
    private LocalDateTime messageSendAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    private ChatRoom chatRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="senderId")
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipientId")
    private User recipient;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public ChatMessage message(String message){
        setMessage(message);
        return this;
    }

    public LocalDateTime getMessageSendAt() {
        return messageSendAt;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    public ChatMessage chatRoom(ChatRoom chatRoom){
        setChatRoom(chatRoom);
        return this;
    }
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    public ChatMessage sender(User sender){
        setSender(sender);
        return this;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    public ChatMessage recipient(User recipient){
        setRecipient(recipient);
        return this;
    }
}
