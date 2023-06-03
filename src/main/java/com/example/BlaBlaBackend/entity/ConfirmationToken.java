package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmationToken")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

    private String userVerifyToken;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public ConfirmationToken() {

    }

    public ConfirmationToken(String userVerifyToken, User userId) {
        this.userVerifyToken = userVerifyToken;
        this.userId = userId;
    }

    public int getCid() {
        return cid;
    }

    public String getUserVerifyToken() {
        return userVerifyToken;
    }

    public User getUserId() {
        return userId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setUserVerifyToken(String userVerifyToken) {
        this.userVerifyToken = userVerifyToken;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
