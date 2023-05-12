package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findUserByEmail(String email);
}
