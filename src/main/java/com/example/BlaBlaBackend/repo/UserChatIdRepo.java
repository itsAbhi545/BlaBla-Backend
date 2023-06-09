package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserChatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatIdRepo extends JpaRepository<UserChatId, User> {

}
