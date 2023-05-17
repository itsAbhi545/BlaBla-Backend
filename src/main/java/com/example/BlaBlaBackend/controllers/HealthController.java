package com.example.BlaBlaBackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @PostMapping("/api/health-check")
    public String healthCheck(HttpServletRequest request){
        String value = request.getHeader("Authorization");
        System.out.println("value =====================" + value);
        return "Ok";
    }
}
