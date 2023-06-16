package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.UserGroupInfo;
import com.example.BlaBlaBackend.repo.UserGroupInfoRepo;
import org.springframework.stereotype.Service;

@Service
public class UserGroupInfoService {
    private final UserGroupInfoRepo userGroupInfoRepo;

    public UserGroupInfoService(UserGroupInfoRepo userGroupInfoRepo) {
        this.userGroupInfoRepo = userGroupInfoRepo;
    }
    public UserGroupInfo saveUserGroupInfo(UserGroupInfo userGroupInfo){
            return userGroupInfoRepo.save(userGroupInfo);
    }
}
