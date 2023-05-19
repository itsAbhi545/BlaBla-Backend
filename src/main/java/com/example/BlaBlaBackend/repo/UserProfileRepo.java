package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserProfile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface UserProfileRepo extends JpaRepository<UserProfile, Integer> {
    @Modifying
    @Query(value = "update userprofile  set userprofile.user_image_url = ?1 where userprofile.user=?2",nativeQuery = true)
    void updateUserImage(String userImageUrl,int user);
    @Query(value = "select * from userprofile where userprofile.user=?1",nativeQuery = true)
    UserProfile findUserProfileByUserId(int user);
}
