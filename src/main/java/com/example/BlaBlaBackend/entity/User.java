package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.util.Regex;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "Email can't be null")
    @Pattern(regexp = Regex.EMAIL,message = "Enter valid email")
    @Column(unique = true)
    private String email;
    @NotNull(message = "Password can't be null")
    @Pattern(regexp = Regex.PASSWORD,message = "Enter valid Password")
    private String password;
    @CreationTimestamp
    private LocalDateTime dateCreated; //epoctime
    @UpdateTimestamp
    private LocalDateTime lastUpdated;
    @Column(columnDefinition = "boolean default false")
    private boolean isVerified;

    public int grabCurrentUserId() {
        return id;
    }


    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String riderPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }
}
