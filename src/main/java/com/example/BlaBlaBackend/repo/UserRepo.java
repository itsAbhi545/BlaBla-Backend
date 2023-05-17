package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface UserRepo extends JpaRepository<User,Integer> {
    User findUserByEmail(String email);
//    @Query("update User user set user.email = ")
//    void updateUserByEmail(String email);
    @Modifying
    @Query("Update User user set user.dob = :#{#currUser.dob} where user.email = :#{#currUser.email}")
    void updateUserProfile(@Param("currUser") User currUser);
}












