package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.util.Regex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="user")
@NoArgsConstructor
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

    @JsonIgnore
    private String password;
    @CreationTimestamp
    private LocalDateTime dateCreated; //epoctime
    @UpdateTimestamp
    private LocalDateTime lastUpdated;
    @Column(columnDefinition = "boolean default false")
    private boolean isVerified;

    @OneToOne(mappedBy="user",cascade = CascadeType.PERSIST,orphanRemoval = true)
    private UserProfile profile;

    public int grabCurrentUserId() {
        return id;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getEmail() {
        return email;
    }
    public boolean userIsVerified(){
        return this.isVerified;
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
