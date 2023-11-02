package com.example.onlinequiz.Controller.PublicController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://quang-nv1911.onrender.com", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public String home() {
        return "Home";
    }
}
