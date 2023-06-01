package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.PasswordResetDto;
import com.example.BlaBlaBackend.Dto.UserDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.*;
import com.example.BlaBlaBackend.repo.UserRepo;
import com.example.BlaBlaBackend.util.Helper;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
@Slf4j
@Service
public class UserService implements UserDetailsService{
    private final UserRepo userRepo;
    private final JwtProvider jwtProvider;
    private final UserProfileService userProfileService;
    private final PasswordService passwordService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserTokensService userTokensService;
    @Value("${address}")
    String currentDomain;

    public UserService(UserRepo userRepo, JwtProvider jwtProvider, UserProfileService userProfileService, PasswordService passwordService, EmailService emailService, ConfirmationTokenService confirmationTokenService, UserTokensService userTokensService) {
        this.userRepo = userRepo;
        this.jwtProvider = jwtProvider;
        this.userProfileService = userProfileService;
        this.passwordService = passwordService;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.userTokensService = userTokensService;
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
        if(!user.userIsVerified()) throw new ApiException(HttpStatus.valueOf(401),"Please verify your Email");

        Collection<SimpleGrantedAuthority> authorites=new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.riderPassword(), authorites);
    }
    //method for getting user image url
    public String getUserImage_Url(String token){
        String uid = jwtProvider.getUsernameFromToken(token);
        String userImageUrl = userProfileService.findUserProfileByUserId(Integer.parseInt(uid)).getUserImageUrl();
        return (userImageUrl!=null)?userImageUrl:"/images/c6c0b9b2-5162-4c01-9d57-e7921839ed19.jpg";
    }
    public void forgetPasswordUtil(String email) throws MessagingException {
        User user = userRepo.findUserByEmail(email);
        if(user == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please enter a Valid email!!!");
        String url = currentDomain + "/users/password/edit?token=";
        //Added Random uuid
        PasswordReset passwordReset = passwordService.addUuidByEmail(user);

        String token = passwordReset.getUuid();
        url += token;

       String message = String.format( """
                <p>Click Below To Reset Your Password</p>
                <a href=%s style="display: inline-block; text-decoration: none; background: #10A37F; border-radius: 3px; color: white; font-family: Helvetica, sans-serif; font-size: 16px; line-height: 24px; font-weight: 400; padding: 12px 20px 11px; margin: 0px" target="_blank" rel="noreferrer">Verify Your Email</a>   
                """,url);
        emailService.sendPasswordResetLink(email, "Reset Your Password",  message);
    }
    public void uploadUserImage(MultipartFile file, String token) throws IOException {
        String folder = "/springBoot projects/BlaBla-Backend/src/main/java/images/";
        //String folder = "/images/";
        byte[] bytes = file.getBytes();
        String uid = jwtProvider.getUsernameFromToken(token);
        String img_url = userProfileService.findUserProfileByUserId(Integer.parseInt(uid)).getUserImageUrl();
        String randomId = (img_url==null)? UUID.randomUUID().toString(): Helper.extractUUid(img_url);

        //finding the extension of file!!!
        String fileExtension = Helper.findExtension(file.getOriginalFilename());
        // Path path = Paths.get(folder + file.getOriginalFilename());
        String url = folder + randomId + fileExtension;
        Path path = Paths.get(url);

        userProfileService.updateUserImage("/images/"+randomId+fileExtension,Integer.parseInt(uid));
        Files.write(path,bytes);
    }
    public User createUser(UserDto userDto){
        //User user1  = objectMapper.convertValue(userDto,User.class);

        User user = new User();
        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userDto,user);
        BeanUtils.copyProperties(userDto,userProfile);
        //saving the user!!!
        User currentUser = this.saveUser(user);
        userProfile.setUser(currentUser);

        //saving the user profile
        userProfileService.saveUserProfile(userProfile);

        //saving the user confirmation Token
        UUID uuid = UUID.randomUUID();
        ConfirmationToken confirmationToken = new ConfirmationToken(uuid.toString(),currentUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return currentUser;
    }
    public UserDto updateUser(UserProfile userProfile,String token,String email){
        if(userProfile.getPhoneNumber()==null)
            throw new ApiException(HttpStatus.valueOf(400),"Please Enter Phone-Number");
        String uid = jwtProvider.getUsernameFromToken(token);
        userProfileService.updateUserProfile(userProfile,Integer.parseInt(uid));
        log.info("User Updated Successfully!!!");
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userProfile,userDto);
        userDto.setPhoneNumber(userProfile.getPhoneNumber());
        userDto.setEmail(email);
        return  userDto;
    }
    public void resetPassword(PasswordResetDto passwordResetDto){
        String newPassword = passwordResetDto.getPassword();
        String cnfPassword = passwordResetDto.getPassword_confirmation();

        String token = passwordResetDto.getReset_password_token();
        PasswordReset passwordReset = passwordService.getByToken(token);
        if(passwordReset == null)
            throw new ApiException(HttpStatus.valueOf(400),"Please Verify Your Email To Continue");
        if(!newPassword.equals(cnfPassword))
            throw new ApiException(HttpStatus.BAD_REQUEST,"Password does Not Matched");

        //User user = userRepo.findUserByEmail(passwordReset.getUser().getEmail());
        User user = passwordReset.getUser();
        user.setPassword(newPassword);
        userRepo.save(user);
//       delete token
        passwordService.deleteTokenByUser(user);
    }
    public UserDto verifyUserAccount(String token){
        ConfirmationToken confirmationToken = (token==null)?null:confirmationTokenService.findConfirmationTokenByUserVerifyToken(token);
        if(confirmationToken==null) throw new ApiException(HttpStatus.BAD_REQUEST,"Enter Valid Token");

        //verifying the user
        User user = confirmationToken.getUserId();
        user.setVerified(true);
        //updating the user
        userRepo.save(user);

        confirmationToken.setUserVerifyToken(null);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //creating a new user token!!
        UserTokens userTokens = new UserTokens();
        userTokens.setUserId(user);

        //saving the user token
        userTokens.setToken(jwtProvider.generateToken(Integer.toString(user.grabCurrentUserId())));
        userTokensService.saveUserToken(userTokens);

        //grab the current user-Profile
        UserProfile userProfile = userProfileService.findUserProfileByUserId(user.grabCurrentUserId());
        UserDto userDto = new UserDto();

        //copying the user details in its dto
        BeanUtils.copyProperties(userProfile,userDto);
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}
