package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private ApiResponse apiResponse;
    private final JwtProvider jwtProvider;
    private final UserTokensService userTokensService;

    public UserController(UserService userService, JwtProvider jwtProvider, UserTokensService userTokensService,ApiResponse apiResponse) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.userTokensService = userTokensService;
        this.apiResponse = apiResponse;
    }

    //route for creating the rider
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<Object,Object> createRider(@RequestBody @Valid User user){
        User user1 =  userService.createRider(user);
        apiResponse.setMessage("User Created Successfully!!");
        apiResponse.setData(user1);
        apiResponse.setHttpStatus(HttpStatus.CREATED);
        System.out.println("Control reaches here!!");
        //saving the user token --creating the user token
        UserTokens userTokens = new UserTokens();
        userTokens.setUserId(user1);
        userTokens.setToken(jwtProvider.generateToken(user1.getEmail()));
        userTokensService.saveUserToken(userTokens);
        HashMap<Object,Object> response = new HashMap<>();
        response.put("token",userTokens.getToken());

        response.put("details",apiResponse);
        return response;
    }
    //route for deleting the user
    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse logoutUser(HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization").substring(7);
        userTokensService.deleteUserTokensByToken(jwtToken);
        apiResponse.setMessage("User Logout successfully!!");
        apiResponse.setHttpStatus(HttpStatus.ACCEPTED);
        return apiResponse;
    }
}
