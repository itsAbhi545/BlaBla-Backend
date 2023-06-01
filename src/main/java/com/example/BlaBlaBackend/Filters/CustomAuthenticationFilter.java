package com.example.BlaBlaBackend.Filters;

import com.example.BlaBlaBackend.Dto.UserDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserProfile;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.UserProfileService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import com.example.BlaBlaBackend.util.Regex;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserTokensService userTokensService;
    private final ObjectMapper objectMapper;
    private final UserProfileService userProfileService;
  //  private final UserTok


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService, UserTokensService userTokensService, ObjectMapper objectMapper, UserProfileService userProfileService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.userTokensService = userTokensService;
        this.objectMapper = objectMapper;
        this.userProfileService = userProfileService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(!Regex.matchEmail(email) || !Regex.matchPassword(password))
            throw new ApiException(HttpStatus.valueOf(400),"Please Enter valid Credentials!!");
        //System.out.println(email);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String email = request.getParameter("email");
        System.out.println(email);
        //extracting the current user id from user table
        User currentUser = userService.findUserByEmail(email);
        System.out.println(currentUser);
        UserTokens userTokens = new UserTokens();
        String jwtToken = jwtProvider.generateToken(Integer.toString(currentUser.grabCurrentUserId()));
        userTokens.setToken(jwtToken);
        userTokens.setUserId(currentUser);
        UserProfile currentUserProfile = userProfileService.findUserProfileByUserId(currentUser.grabCurrentUserId());
        UserDto userDto = new UserDto();
        //saving the token in usertoken table corresponding to current logged in user!!
        userTokensService.saveUserToken(userTokens);
        BeanUtils.copyProperties(currentUser,userDto);
        BeanUtils.copyProperties(currentUserProfile,userDto);
        Map<String,Object> authToken = new HashMap<>();
        authToken.put("token",jwtToken);
        authToken.put("detail",userDto);

        new ObjectMapper().writeValue(response.getOutputStream(),authToken);
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//     //   AuthenticationFailureHandler
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        Map<String, Object> map = new HashMap<>();
//        // System.out.println();
//        map.put("message", failed.getLocalizedMessage());
//        map.put("code", 401);
//        map.put("msg", failed.getMessage());
//        map.put("path", request.getServletPath());
//        map.put("timestamp", System.currentTimeMillis());
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        new ObjectMapper().writeValue(response.getOutputStream(),map);
//    }
}
