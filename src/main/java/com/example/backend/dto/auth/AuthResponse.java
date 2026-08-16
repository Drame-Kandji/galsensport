package com.example.backend.dto.auth;

import com.example.backend.entity.Role;

public class AuthResponse {

    private Long id;
    private String email;
    private String telephone;
    private Role role;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(
            Long id,
            String email,
            String telephone,
            Role role,
            String token
    ) {
        this.id = id;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}