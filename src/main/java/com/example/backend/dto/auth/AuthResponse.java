package com.example.backend.dto.auth;

import com.example.backend.dto.user.UserResponse;

public class AuthResponse {

    private String token;

    private String refreshToken;

    private long expiresIn;

    private UserResponse user;


    public AuthResponse() {
    }


    public AuthResponse(
            String token,
            String refreshToken,
            long expiresIn,
            UserResponse user
    ) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }


    public String getToken() {
        return token;
    }

    public String getRefreshToken() { return refreshToken; }

    public long getExpiresIn() { return expiresIn; }


    public UserResponse getUser() {
        return user;
    }
}
