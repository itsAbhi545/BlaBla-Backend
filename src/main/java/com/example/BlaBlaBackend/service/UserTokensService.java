package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.repo.UserTokensRepo;
import org.springframework.stereotype.Service;

@Service
public class UserTokensService {
    private final UserTokensRepo userTokensRepo;

    public UserTokensService(UserTokensRepo userTokensRepo) {
        this.userTokensRepo = userTokensRepo;
    }
    public UserTokens saveUserToken(UserTokens userTokens){
        return userTokensRepo.save(userTokens);
    }
    public UserTokens findUserTokensByToken(String token){
        return userTokensRepo.findUserTokensByToken(token);
    }
    public void deleteUserTokensByToken(String token){ userTokensRepo.deleteUserTokensByToken(token); }
}
