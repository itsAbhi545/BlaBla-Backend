package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.UserDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.*;
import com.example.BlaBlaBackend.service.ConfirmationTokenService;
import com.example.BlaBlaBackend.service.UserProfileService;
import com.example.BlaBlaBackend.service.EmailService;
import com.example.BlaBlaBackend.service.PasswordService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import com.example.BlaBlaBackend.util.Helper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
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
@Slf4j
public class UserController {
    private final UserService userService;
    private final ApiResponse apiResponse;
    private final JwtProvider jwtProvider;
    private final UserTokensService userTokensService;
    private final PasswordService passwordService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserProfileService userProfileService;

    @Value("${address}")
    String currentDomain;
    //private final ObjectMapper objectMapper;


    public UserController(UserService userService, ApiResponse apiResponse, JwtProvider jwtProvider, UserTokensService userTokensService, PasswordService passwordService, EmailService emailService, ObjectMapper objectMapper, ConfirmationTokenService confirmationTokenService, UserProfileService userProfileService) {
        this.userService = userService;
        this.apiResponse = apiResponse;
        this.jwtProvider = jwtProvider;
        this.userTokensService = userTokensService;
        this.passwordService = passwordService;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.confirmationTokenService = confirmationTokenService;
        this.userProfileService = userProfileService;
    }

    //route for creating the rider
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<Object,Object> createUser(@RequestBody @Valid UserDto userDto){

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
        if(user!=null){
            throw  new ApiException(HttpStatus.valueOf(403),"User Already Exist!!!");
        }
        apiResponse.setMessage("User doesn't exist!!!");
        apiResponse.setHttpStatus(HttpStatus.OK);
        return apiResponse;
    }
    //route for updating the user profile
    @PatchMapping("/update/user")
    public ApiResponse updateUser(@Valid UserProfile userProfile, HttpServletRequest request,Principal principal){
        if(userProfile.getPhoneNumber()==null)
            throw new ApiException(HttpStatus.valueOf(400),"Please Enter Phone-Number");
        apiResponse.setMessage("User Updated Successfully!!");
        apiResponse.setHttpStatus(HttpStatus.valueOf(202));
        String token = request.getHeader("Authorization").substring(7);
        String uid = jwtProvider.getUsernameFromToken(token);
       // user.setEmail(principal.getName());
       // userService.updateUserProfile(user);
        userProfileService.updateUserProfile(userProfile,Integer.parseInt(uid));
        log.info("User Updated Successfully!!!");
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userProfile,userDto);
        userDto.setPhoneNumber(userProfile.getPhoneNumber());
        userDto.setEmail(principal.getName());
        apiResponse.setData(userDto);
//        String email = userProfile.getUser().getEmail();
        return  apiResponse;
    }
    // Forgot Password
    @PostMapping("/forgetPassword")
    public ApiResponse forgetPassword(@RequestParam("email") String email) throws MessagingException {
        String url = currentDomain + "api/checkLinkPasswordReset/";
        //Added Random uuid
        PasswordReset passwordReset = passwordService.addUuidByEmail(email);

        String token = passwordReset.getUuid();
        url += token +  "?email=" + email;

        String message = "<p>Click Below To Reset Your Password</p>\n" +
                "\n" +
                "<a href=\""+ url + "\"" +
                "style=\"display: inline-block; text-decoration: none; background: #10A37F; border-radius: 3px; color: white; font-family: Helvetica, sans-serif; font-size: 16px; line-height: 24px; font-weight: 400; padding: 12px 20px 11px; margin: 0px\" target=\"_blank\" rel=\"noreferrer\">Verify Your Email</a> ";

        emailService.sendPasswordResetLink(email, "Reset Your Password",  message);

        apiResponse.setMessage("Verification Link Send Successfully");
        return  apiResponse;
    }

    @PostMapping("/resetPassword")
    public ApiResponse resetPassword(@RequestParam("email") String email,HttpServletRequest request) {
        String newPassword = request.getParameter("password");
        String cnfPassword = request.getParameter("cnfpassword");
        if(newPassword == null || cnfPassword == null) {
            apiResponse.setMessage("Please Enter The Password");
            return apiResponse;
        }
        PasswordReset passwordReset = passwordService.getByEmail(email);
        if(passwordReset != null) {
            if (newPassword.equals(cnfPassword) && passwordReset.getIsVerify()) {
                User user = userService.findUserByEmail(email);
                user.setPassword(newPassword);
                userService.saveUser(user);
//            delete token
                passwordService.deleteTokenByEmail(email);
                apiResponse.setMessage("Password Reset Successfully");
                apiResponse.setHttpStatus(HttpStatus.ACCEPTED);
                return apiResponse;
            } else if (!passwordReset.getIsVerify()) {
                apiResponse.setMessage("Link Not Verified");
                return apiResponse;
            } else {
                apiResponse.setMessage("Password does Not Matched");
                return apiResponse;
            }
        }
        else {
            apiResponse.setMessage("Please Verify Your Email To Continue");
            return apiResponse;
        }
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
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile file, HttpServletRequest request) throws IOException {
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
