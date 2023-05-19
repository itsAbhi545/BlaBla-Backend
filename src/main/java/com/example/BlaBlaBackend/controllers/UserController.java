package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.UserDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.ConfirmationToken;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserProfile;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.ConfirmationTokenService;
import com.example.BlaBlaBackend.service.UserProfileService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import com.example.BlaBlaBackend.util.Helper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private  ApiResponse apiResponse;
    private final JwtProvider jwtProvider;
    private final UserTokensService userTokensService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ObjectMapper objectMapper;
    private final UserProfileService userProfileService;

    public UserController(UserService userService, ApiResponse apiResponse, JwtProvider jwtProvider, UserTokensService userTokensService, ConfirmationTokenService confirmationTokenService, ObjectMapper objectMapper, UserProfileService userProfileService) {
        this.userService = userService;
        this.apiResponse = apiResponse;
        this.jwtProvider = jwtProvider;
        this.userTokensService = userTokensService;
        this.confirmationTokenService = confirmationTokenService;
        this.objectMapper = objectMapper;
        this.userProfileService = userProfileService;
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    //route for creating the rider
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<Object,Object> createRider(@RequestBody @Valid UserDto userDto){

        User user1  = objectMapper.convertValue(userDto,User.class);
        UserProfile userProfile = objectMapper.convertValue(userDto,UserProfile.class);

        apiResponse.setMessage("User Created Successfully!!");
        apiResponse.setData(user1);
        apiResponse.setHttpStatus(HttpStatus.OK);

        //saving the user!!!
        user1 = userService.saveUser(user1);
        userProfile.setUser(user1);
        //saving the user profile
        userProfileService.saveUserProfile(userProfile);

        UserTokens userTokens = new UserTokens();
        userTokens.setUserId(user1);
        userTokens.setToken(jwtProvider.generateToken(user1.getEmail()));
        userTokensService.saveUserToken(userTokens);

        //saving the user confirmation Token
        UUID uuid = UUID.randomUUID();
        ConfirmationToken confirmationToken = new ConfirmationToken(uuid.toString(),user1);
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
    public ApiResponse userExist(String email){
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

    //route for updating the user profile
    @PatchMapping("/update/user")
    public ApiResponse updateUser(UserProfile userProfile,Principal principal){
        apiResponse.setMessage("User Updated Successfully!!");
        apiResponse.setHttpStatus(HttpStatus.valueOf(204));
       // user.setEmail(principal.getName());
       // userService.updateUserProfile(user);
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
    //route for uploading user avatar!!!!
    @PostMapping("/upload/user-image")
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile file,HttpServletRequest request) throws IOException {
        String folder = "/springBoot projects/BlaBla-Backend/src/main/java/images/";
        byte[] bytes = file.getBytes();
        String token = request.getHeader("Authorization").substring(7);
        String uid = jwtProvider.getUsernameFromToken(token);
       // System.out.println(uid+"///");
        //finding the extension of file!!!
        String fileExtension = Helper.findExtension(file.getOriginalFilename());
        // Path path = Paths.get(folder + file.getOriginalFilename());
        String url = folder + uid + fileExtension;
        Path path = Paths.get(url);

        userProfileService.updateUserImage(url,Integer.parseInt(uid));
        Files.write(path,bytes);
        apiResponse.setMessage("Image Uploaded Successfully!!");
        apiResponse.setHttpStatus(HttpStatus.valueOf(201));
        return apiResponse;
    }
    //route for displaying user image
    @GetMapping(value ="/show/user-image",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] downloadImage(HttpServletRequest request) throws IOException {
        String token = request.getHeader("Authorization").substring(7);
        String uid =  jwtProvider.getUsernameFromToken(token);
        System.out.println("\u001B[34m" + uid + "\u001B[0m");
        String filePath = userProfileService.findUserProfileByUserId(Integer.parseInt(uid)).getUserImageUrl();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
