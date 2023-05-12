package com.example.BlaBlaBackend.Filters;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
  //  private final UserTok


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService, UserTokensService userTokensService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.userTokensService = userTokensService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String jwtToken = jwtProvider.generateToken(request.getHeader("email"));
        String email = request.getParameter("email");
        //extracting the current user id from user table
        User currentUser = userService.findUserByEmail(email);
        UserTokens userTokens = new UserTokens();
        userTokens.setToken(jwtToken);
        userTokens.setUserId(currentUser);
        //saving the token in usertoken table corresponding to current logged in user!!
        userTokensService.saveUserToken(userTokens);
        Map<String,Object> authToken = new HashMap<>();
        authToken.put("token",jwtToken);
        authToken.put("detail",currentUser);
        new ObjectMapper().writeValue(response.getOutputStream(),authToken);
    }
}
