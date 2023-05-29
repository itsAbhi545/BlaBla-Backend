package com.example.BlaBlaBackend.repo;


import com.example.BlaBlaBackend.entity.PasswordReset;
import com.example.BlaBlaBackend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepo extends JpaRepository<PasswordReset, Integer> {
    PasswordReset getPasswordResetByUuid(String uuid);
    PasswordReset getPasswordResetByUser(User user);
    int deleteByUuid(String uuid);
    @Transactional
    void deletePasswordResetByUser(User user);
}
