package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name="usergroupinfo")
public class UserGroupInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private ChatRoom chatRoom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public UserGroupInfo user(User user){
        setUser(user);
        return this;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    public UserGroupInfo chatRoom(ChatRoom chatRoom){
        setChatRoom(chatRoom);
        return this;
    }
}
