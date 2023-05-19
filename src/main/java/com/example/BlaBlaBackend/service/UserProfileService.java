package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.UserProfile;
import com.example.BlaBlaBackend.repo.UserProfileRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService{
    private final UserProfileRepo userProfileRepo;

    public UserProfileService(UserProfileRepo userProfileRepo) {
        this.userProfileRepo = userProfileRepo;
    }
    //saving the user-Profile
    public UserProfile saveUserProfile(UserProfile userProfile){
        return userProfileRepo.save(userProfile);
    }
    public void updateUserImage(String url,int user){
        userProfileRepo.updateUserImage(url,user);
    }
    public UserProfile findUserProfileByUserId(int id){
        return userProfileRepo.findUserProfileByUserId(id);
    }
}
