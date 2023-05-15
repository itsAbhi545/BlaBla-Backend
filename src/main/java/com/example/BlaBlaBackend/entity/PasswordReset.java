package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserPasswordReset", uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String email;
    String uuid;

    @Column(columnDefinition = "boolean default false")
    private Boolean isVerify;
}
