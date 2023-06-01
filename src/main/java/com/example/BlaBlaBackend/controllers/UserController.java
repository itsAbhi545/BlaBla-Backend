package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.PasswordResetDto;
import com.example.BlaBlaBackend.Dto.UserDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserProfile;
import com.example.BlaBlaBackend.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserTokensService userTokensService;

    public UserController(UserService userService, UserTokensService userTokensService) {
        this.userService = userService;
        this.userTokensService = userTokensService;
    }

    //route for creating the rider
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createUser(@RequestBody @Valid UserDto userDto){
        User user = userService.createUser(userDto);
        return ApiResponse.builder().message("User Created Successfully!!")
                .data(user).httpStatus(HttpStatus.CREATED).build();
    }
    //route for deleting the user
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse logoutUser(@RequestHeader String Authorization){
        userTokensService.deleteUserTokensByToken(Authorization.substring(7));
        return ApiResponse.builder().message("User Logout successfully!!")
                .httpStatus(HttpStatus.ACCEPTED).build();
    }
    //route for verifying the user by email
    @PostMapping("/check/user-email-exist")
    public ApiResponse userExist(String email){
        log.info("\u001B[31m"+email+"\u001B[0m");
        User user = userService.findUserByEmail(email);
        if(user!=null)
            throw  new ApiException(HttpStatus.valueOf(403),"User Already Exist!!!");
        return ApiResponse.builder().message("User doesn't exist!!!")
                .httpStatus(HttpStatus.OK).build();
    }
    //route for updating the user profile
    @PatchMapping("/update/user")
    public ApiResponse updateUser(UserProfile userProfile,@RequestHeader String Authorization,Principal principal){
        UserDto userDto =  userService.updateUser(userProfile,Authorization.substring(7),principal.getName());

        return  ApiResponse.builder().message("User Updated Successfully!!")
                .httpStatus(HttpStatus.valueOf(202)).data(userDto)
                .build();
    }

    // Forgot Password
    @PostMapping("/forgetPassword")
    public ApiResponse forgetPassword(@RequestParam("email") String email) throws MessagingException {
        userService.forgetPasswordUtil(email);
        return  ApiResponse.builder().message("Verification Link Send Successfully")
                .httpStatus(HttpStatus.OK).build();
    }

    @PostMapping("/resetPassword")
    public ApiResponse resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) {
        userService.resetPassword(passwordResetDto);
        return ApiResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Password Reset Successfully").build();
    }

    //route for verifying the user!!!
    @PostMapping("/verify-user/email/{token}")
    public ApiResponse verifyUserAccount(@PathVariable String token){
        UserDto userDto = userService.verifyUserAccount(token);
//        ConfirmationToken confirmationToken = (token==null)?null:confirmationTokenService.findConfirmationTokenByUserVerifyToken(token);
//        if(confirmationToken==null) throw new ApiException(HttpStatus.BAD_REQUEST,"Enter Valid Token");
//
//        //verifying the user
//        User user = confirmationToken.getUserId();
//        user.setVerified(true);
//        //updating the user
//        userService.saveUser(user);
//
//        confirmationToken.setUserVerifyToken(null);
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//       //creating a new user token!!
//        UserTokens userTokens = new UserTokens();
//        userTokens.setUserId(user);
//        //saving the user token
//        userTokens.setToken(jwtProvider.generateToken(Integer.toString(user.grabCurrentUserId())));
//        userTokensService.saveUserToken(userTokens);
//        //grab the current user-Profile
//        UserProfile userProfile = userProfileService.findUserProfileByUserId(user.grabCurrentUserId());
//        UserDto userDto = new UserDto();
//
//        //copying the user details in its dto
//        BeanUtils.copyProperties(userProfile,userDto);
//        userDto.setEmail(user.getEmail());
        return ApiResponse.builder().httpStatus(HttpStatus.OK).message("User verified Successfully!!").data(userDto).build();
    }

    //route for uploading user avatar!!!!
    @PostMapping("/upload/user-image")
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile file,@RequestHeader String Authorization) throws IOException {
        userService.uploadUserImage(file,Authorization.substring(7));
        // String folder = "/springBoot projects/BlaBla-Backend/src/main/java/images/";
//      {
// String folder = "/images/";
//        byte[] bytes = file.getBytes();
//        String token = request.getHeader("Authorization").substring(7);
//        String uid = jwtProvider.getUsernameFromToken(token);
//        String img_url = userProfileService.findUserProfileByUserId(Integer.parseInt(uid)).getUserImageUrl();
//        String randomId = (img_url==null)?UUID.randomUUID().toString():Helper.extractUUid(img_url);
//       // System.out.println(uid+"///");
//        //finding the extension of file!!!
//        String fileExtension = Helper.findExtension(file.getOriginalFilename());
//        // Path path = Paths.get(folder + file.getOriginalFilename());
//        String url = folder + randomId + fileExtension;
//        Path path = Paths.get(url);
//
//        userProfileService.updateUserImage("/images/"+randomId+fileExtension,Integer.parseInt(uid));
//        Files.write(path,bytes);
        return ApiResponse.builder().message("Image Uploaded Successfully!!")
                .httpStatus(HttpStatus.valueOf(201)).build();
    }
    //route for displaying user image
//    @GetMapping(value ="/show/user-image",produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] downloadImage(HttpServletRequest request) throws IOException {
//        String token = request.getHeader("Authorization").substring(7);
//        String uid =  jwtProvider.getUsernameFromToken(token);
//        System.out.println("\u001B[34m" + uid + "\u001B[0m");
//        String filePath = userProfileService.findUserProfileByUserId(Integer.parseInt(uid)).getUserImageUrl();
//        if(filePath==null) throw new ApiException(HttpStatus.valueOf(400),"Please Upload your Image!!!");
//        byte[] images = Files.readAllBytes(new File(filePath).toPath());
//        return images;
//    }
    //route for displaying user image
    @GetMapping(value = "/show/user-image")
    public ApiResponse getUserImage_Url(@RequestHeader String Authorization){
        String imageUrl = userService.getUserImage_Url(Authorization.substring(7));
        return ApiResponse.builder().data(imageUrl)
                .message("User Avatar Link")
                .httpStatus(HttpStatus.OK).build();
    }
}
