package com.example.backend.controller;

import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.User;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public UserResponse me(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new UserResponse(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole().name()
        );
    }
}