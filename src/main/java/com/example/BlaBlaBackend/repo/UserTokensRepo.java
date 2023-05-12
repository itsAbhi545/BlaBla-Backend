package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.UserTokens;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserTokensRepo extends JpaRepository<UserTokens,Integer> {
    UserTokens findUserTokensByToken(String token);
    @Transactional
    @Modifying
    void deleteUserTokensByToken(String token);
}
