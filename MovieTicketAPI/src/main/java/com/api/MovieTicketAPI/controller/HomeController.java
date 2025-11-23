package com.api.MovieTicketAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Movie Ticket API is running successfully!";
    }
    
    @GetMapping("/health")
    public String healthCheck() {
        return "API Health: OK";
    }
}
