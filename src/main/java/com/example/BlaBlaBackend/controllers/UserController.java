package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.UserDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.ConfirmationToken;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.ConfirmationTokenService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private ApiResponse apiResponse;
    private final JwtProvider jwtProvider;
    private final UserTokensService userTokensService;
    private final ConfirmationTokenService confirmationTokenService;
    //private final ObjectMapper objectMapper;

    public UserController(UserService userService, ApiResponse apiResponse, JwtProvider jwtProvider, UserTokensService userTokensService, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.apiResponse = apiResponse;
        this.jwtProvider = jwtProvider;
        this.userTokensService = userTokensService;
        this.confirmationTokenService = confirmationTokenService;
    }


    //route for creating the rider
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<Object,Object> createRider(@RequestBody @Valid User user){
        User user1 =  userService.saveUser(user);
        apiResponse.setMessage("User Created Successfully!!");
        apiResponse.setData(user1);
        apiResponse.setHttpStatus(HttpStatus.CREATED);
        System.out.println("Control reaches here!!");
       // String title = user.getTitle().toUpperCase().equals("MISS") ? "FEMALE":"MALE";
        if(user.getTitle()!=null)
         user.setTitle(user.getTitle().toUpperCase().equals("MISS") ? "FEMALE":"MALE");
        //saving the user token --creating the user token
        UserTokens userTokens = new UserTokens();
        userTokens.setUserId(user1);
        userTokens.setToken(jwtProvider.generateToken(user1.getEmail()));
        userTokensService.saveUserToken(userTokens);

        //saving the user confirmation Token
        UUID uuid = UUID.randomUUID();
        ConfirmationToken confirmationToken = new ConfirmationToken(uuid.toString(),user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        HashMap<Object,Object> response = new HashMap<>();
        response.put("token",userTokens.getToken());

        response.put("details",apiResponse);
        return response;
    }
    //route for deleting the user
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse logoutUser(HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization").substring(7);
        userTokensService.deleteUserTokensByToken(jwtToken);
        apiResponse.setMessage("User Logout successfully!!");
        apiResponse.setHttpStatus(HttpStatus.ACCEPTED);
        return apiResponse;
    }
    //route for verifying the user by email
    @PostMapping("/verify-user/email")
    public ApiResponse userExist(@RequestBody String email){
        System.out.println("\u001B[31m"+email+"\u001B[0m");
        User user = userService.findUserByEmail(email);
        System.out.println(email);
        if(user!=null){
            throw  new ApiException(HttpStatus.valueOf(403),"User Already Exist!!!");
        }
        apiResponse.setMessage("User doesn't exist!!!");
        apiResponse.setHttpStatus(HttpStatus.OK);
        return apiResponse;
    }
    @PatchMapping("/update/user")
    public ApiResponse updateUser(User user){
        apiResponse.setMessage("User Updated Successfully!!");
        apiResponse.setHttpStatus(HttpStatus.valueOf(204));
        userService.updateUserProfile(user);
        return  apiResponse;
    }
    //route for verifying the user!!!
    @GetMapping("/confirm-account/{token}")
    public ApiResponse ConfirmUserAccount(@PathVariable String token){
        if(token==null) throw new ApiException(HttpStatus.BAD_REQUEST,"Enter Valid Token");
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationTokenByUserVerifyToken(token);
        if(confirmationToken==null) throw new ApiException(HttpStatus.BAD_REQUEST,"Enter Valid Token");
        apiResponse.setHttpStatus(HttpStatus.OK);
        apiResponse.setMessage("User verified Successfully!!");

        //verifying the user
        User user = confirmationToken.getUserId();
        user.setVerified(true);
        //updating the user
        userService.saveUser(user);

        confirmationToken.setUserVerifyToken(null);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return apiResponse;
    }
}
