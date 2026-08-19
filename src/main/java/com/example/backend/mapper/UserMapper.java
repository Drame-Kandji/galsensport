package com.example.backend.mapper;

import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    // =========================================================
    // USER RESPONSE
    // =========================================================

    public UserResponse toUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole(),
                null,
                null
        );
    }


    // =========================================================
    // AUTH RESPONSE
    // =========================================================

    public AuthResponse toAuthResponse(
            User user,
            String token
    ) {

        UserResponse userResponse =
                toUserResponse(user);

        return new AuthResponse(
                token,
                userResponse
        );
    }
}