package com.example.bookstore.controller;

import com.example.bookstore.dto.UserDto;
import com.example.bookstore.dto.UserRegistrationRequestDto;
import com.example.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public UserDto registration(@RequestBody UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }
}
