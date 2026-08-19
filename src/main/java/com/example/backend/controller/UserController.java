package com.example.backend.controller;

import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.service.user.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retourne les informations de l'utilisateur connecté.
     */
    @GetMapping("/me")
    //@PreAuthorize("hasRole('USER')")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getMe(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                userService.getCurrentUserResponse(
                        user.getId()
                )
        );
    }
}
