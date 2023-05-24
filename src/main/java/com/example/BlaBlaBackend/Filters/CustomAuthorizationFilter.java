package com.example.BlaBlaBackend.Filters;

import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.UserTokens;
import com.example.BlaBlaBackend.service.UserTokensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final UserTokensService userTokensService;
    private final JwtProvider jwtProvider;

    public CustomAuthorizationFilter(UserTokensService userTokensService, JwtProvider jwtProvider) {
        this.userTokensService = userTokensService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println(request.getServletPath()+"///");
        if(request.getServletPath().equals("/api/login")||request.getServletPath().equals("/api/signup")||
        request.getServletPath().equals("/health-check")||request.getServletPath().equals("/favicon.ico")
        ||request.getServletPath().equals("/error")||request.getServletPath().contains("/api/verify-user/email")||
        request.getServletPath().contains("/api/confirm-account")||request.getServletPath().contains("/images/"))
        {
            System.out.println( "\u001B[31m" + request.getServletPath() + "\u001B[0m");
            filterChain.doFilter(request,response);
        }else{
            String token = request.getHeader("Authorization");
            UserTokens userTokens = (token==null)?null:userTokensService.findUserTokensByToken(token.substring(7));
            if(userTokens==null|| !jwtProvider.validateToken(token.substring(7))){
                //then i will simply delete the token
                if(userTokens!=null) {
                    userTokensService.deleteUserTokensByToken(token.substring(7));
                }
                Map<String,String> error=new HashMap<>();
                error.put("error_message","Please provide valid token");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
                return;
            }
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userTokens.getUserId().getEmail(),  null, authorities);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request,response);
        }
    }

}
