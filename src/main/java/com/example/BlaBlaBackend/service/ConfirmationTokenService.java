package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.ConfirmationToken;
import com.example.BlaBlaBackend.repo.ConfirmationTokenRepo;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public ConfirmationTokenService(ConfirmationTokenRepo confirmationTokenRepo) {
        this.confirmationTokenRepo = confirmationTokenRepo;
    }
    //saving the confirmation token in database
    public ConfirmationToken saveConfirmationToken(ConfirmationToken confirmationToken){
        return confirmationTokenRepo.save(confirmationToken);
    }
    public ConfirmationToken findConfirmationTokenByUserVerifyToken(String userVerifyToken){
        return confirmationTokenRepo.findConfirmationTokenByUserVerifyToken(userVerifyToken);
    }
    public void deleteConfirmationTokenByCid(int cid){
        confirmationTokenRepo.deleteById(cid);
    }
}
