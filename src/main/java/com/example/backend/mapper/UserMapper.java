package com.example.backend.mapper;

import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AuthResponse toAuthResponse(
            User user,
            String token
    ) {

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole(),
                token
        );
    }
}