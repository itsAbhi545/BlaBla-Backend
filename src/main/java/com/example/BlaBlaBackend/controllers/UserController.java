package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.PasswordReset;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.EmailService;
import com.example.BlaBlaBackend.service.PasswordService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @Value("${address}")
    String currentDomain;
    //private final ObjectMapper objectMapper;

    public UserController(UserService userService, JwtProvider jwtProvider, UserTokensService userTokensService, ApiResponse apiResponse, PasswordService passwordService, EmailService emailService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.userTokensService = userTokensService;
        this.apiResponse = apiResponse;
        this.passwordService = passwordService;
        this.emailService = emailService;
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
//        if(user.getTitle()!=null)
//            user.setTitle(user.getTitle().toUpperCase().equals("MISS") ? "FEMALE":"MALE");
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
    //route for verifying the user by email
    @GetMapping("/verify-user/email")
    public ApiResponse userExist(String email){
        User user = userService.findUserByEmail(email);
        System.out.println(user);
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
        return  apiResponse;
    }
    // Forgot Password
    @PostMapping("/forgetPassword")

    public ApiResponse forgetPassword(@RequestParam("email") String email, Model model) throws MessagingException {
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
    @GetMapping("/checkLinkPasswordReset/{token}")
    public ApiResponse forgetPassword(@PathVariable("token") String token,
                                 @RequestParam("email")String email){
        System.out.println("scavsghavsavscaghv");
        String tokenFinal = passwordService.getByEmail(email).getUuid();

        if((tokenFinal != null) && (tokenFinal.equals(token))){
            log.info("\u001B[41m" + "here"+ "\u001B[0m");
            PasswordReset passwordReset = passwordService.getByEmail(email);
            passwordReset.setIsVerify(true);
            passwordService.savePasswordReset(passwordReset);
            passwordService.deleteTokenByEmail(email);
            apiResponse.setMessage("Link Verified Successfully");
            apiResponse.setHttpStatus(HttpStatus.CONTINUE);

            return apiResponse;
        }
       throw new ApiException(HttpStatus.BAD_REQUEST, "Link does not Matched");
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

}
