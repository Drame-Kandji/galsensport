package com.example.backend.mapper;

import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.dto.auth.RegisterRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Convertit RegisterRequest vers User
     */
    public User toUser(RegisterRequest request) {

        User user = new User();

        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        user.setPassword(request.getPassword());

        // Tous les nouveaux comptes commencent comme USER
        user.setRole(Role.USER);

        return user;
    }

    /**
     * Convertit User vers UserResponse
     */
    public UserResponse toUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole().name()
        );
    }

    /**
     * Convertit User vers AuthResponse
     */
    public AuthResponse toAuthResponse(User user, String token) {

        return new AuthResponse(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole().name(),
                token
        );
    }
}