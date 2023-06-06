package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken,Integer> {
    ConfirmationToken findConfirmationTokenByUserVerifyToken(String userVerifyToken);

}
