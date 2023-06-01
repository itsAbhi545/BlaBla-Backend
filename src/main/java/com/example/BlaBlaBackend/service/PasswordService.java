package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.PasswordReset;
import com.example.BlaBlaBackend.entity.User;
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

    public PasswordReset addUuidByEmail(User user){

        String uuid = UUID.randomUUID().toString();
        PasswordReset passwordReset = passwordResetRepo.getPasswordResetByUser(user);
        if(passwordReset == null) {
            passwordReset = PasswordReset.builder()
                    .uuid(uuid)
                    .user(user)
                    .build();
        }
        passwordReset.setUuid(uuid);
        return passwordResetRepo.save(passwordReset);
    }
    public PasswordReset getByUser(User user) {

        return passwordResetRepo.getPasswordResetByUser(user);
    }
    public PasswordReset getByToken(String token) {
        return passwordResetRepo.getPasswordResetByUuid(token);

    }
    public void deleteTokenByUser(User user) {
        log.info("\u001B[41m" + "im here"+ "\u001B[0m");

        passwordResetRepo.deletePasswordResetByUser(user);
    }
    public void deleteTokenById(int id){
        passwordResetRepo.deleteById(id);
    }
    public PasswordReset savePasswordReset(PasswordReset passwordReset){
        return passwordResetRepo.save(passwordReset);
    }
}
