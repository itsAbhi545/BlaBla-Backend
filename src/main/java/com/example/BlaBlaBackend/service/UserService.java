package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService{
    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public User saveUser(User user){
        return  userRepo.save(user);
    }
    public User findUserByEmail(String email){
        return userRepo.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (email!=null)?findUserByEmail(email):null;
        System.out.println("User Service");
        if(user ==null){
            throw new UsernameNotFoundException("User not found!!!");
        }
        //this condition will execute when user account is not verified
      //  if(!user.userIsVerified()) throw new ApiException(HttpStatus.valueOf(401),"Please verify your Email");

        Collection<SimpleGrantedAuthority> authorites=new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.riderPassword(), authorites);
    }
//    public void updateUserProfile(User user){
//        userRepo.updateUserProfile(user);
//    }
}
