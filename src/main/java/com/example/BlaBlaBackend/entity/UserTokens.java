package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usertokens")
public class UserTokens {
    @Id
    @GeneratedValue
    private int id;
    @NotNull(message = "token can't be null")
    private String token;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="userId")
    private User userId;
    @CreationTimestamp
    private LocalDateTime dateCreated; //epoctime

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public User getUserId() {
        return userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
