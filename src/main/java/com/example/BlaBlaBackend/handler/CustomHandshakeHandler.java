package com.example.BlaBlaBackend.handler;

import com.example.BlaBlaBackend.Dto.User;
import com.example.BlaBlaBackend.config.JwtProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    // Custom class for storing principal
    private final JwtProvider jwtProvider;

    public CustomHandshakeHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected Principal determineUser(
        ServerHttpRequest request,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) {
        String Authorization = request.getHeaders().get("Authorization").get(0);
        String uid = jwtProvider.getUsernameFromToken(Authorization.substring(7));
       // String userChatId = userChatIdService.getUserChatIdByUserId(Integer.parseInt(uid)).getUserChatId();
        String userChatId ="user_"+uid;
        // Generate principal with UUID as name
        return new User(userChatId);
    }
}