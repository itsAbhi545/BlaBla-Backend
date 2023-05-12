package com.example.BlaBlaBackend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
public class HealthController {
    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }
}
