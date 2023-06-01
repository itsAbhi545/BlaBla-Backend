package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserPasswordReset")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    User user;
    String uuid;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

//    @Column(columnDefinition = "boolean default false")
//    private Boolean isVerify;
}
