package com.example.backend.mapper;

import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.service.auth.AuthenticationTokenService;

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
            AuthenticationTokenService.SessionTokens tokens
    ) {

        UserResponse userResponse =
                toUserResponse(user);

        return new AuthResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.expiresIn(),
                userResponse
        );
    }
}
