package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findUserByEmail(String email);
//    @Query("update User user set user.email = ")
//    void updateUserByEmail(String email);
}
