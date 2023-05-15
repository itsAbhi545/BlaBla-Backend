package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.PasswordReset;
import com.example.BlaBlaBackend.repo.PasswordResetRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PasswordService {
    @Autowired
    PasswordResetRepo passwordResetRepo;

    public PasswordReset addUuidByEmail(String email){
        String uuid = UUID.randomUUID().toString();
        PasswordReset passwordReset = PasswordReset.builder()
                .uuid(uuid)
                .email(email)
                .build();
        return passwordResetRepo.save(passwordReset);
    }
    public PasswordReset getByEmail(String email) {
         return passwordResetRepo.getPasswordResetByEmail(email);
    }
    public void deleteTokenByEmail(String email) {
        passwordResetRepo.deleteByEmail(email);
    }
}
