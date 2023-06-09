package com.example.BlaBlaBackend.handler;

import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    // Custom class for storing principal

//    @Override
//    protected Principal determineUser(
//        ServerHttpRequest request,
//        WebSocketHandler wsHandler,
//        Map<String, Object> attributes
//    ) {
//        System.out.println("control reaches here!!!");
//        // Generate principal with UUID as name
////        return new User("user");
//    }
}