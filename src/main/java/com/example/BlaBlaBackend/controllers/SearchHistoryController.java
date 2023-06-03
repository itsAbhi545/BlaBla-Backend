package com.example.BlaBlaBackend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchHistoryController {
    @RequestMapping("/search/ride/history")
    public String allSearchHistory(){

        return null;
    }
}
