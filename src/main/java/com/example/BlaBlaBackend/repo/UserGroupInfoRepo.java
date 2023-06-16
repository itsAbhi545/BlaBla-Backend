package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.UserGroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupInfoRepo extends JpaRepository<UserGroupInfo,Integer> {
}
