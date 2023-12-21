package com.example.bookstore.service;

import com.example.bookstore.dto.UserDto;
import com.example.bookstore.dto.UserRegistrationRequestDto;

public interface UserService {
    UserDto register(UserRegistrationRequestDto requestDto);
}
