package com.example.backend.controller;

import com.example.backend.dto.admin.AdminProfileRequest;
import com.example.backend.dto.admin.AdminProfileResponse;
import com.example.backend.entity.User;
import com.example.backend.service.admin.AdminProfileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/profile")
@SecurityRequirement(name = "bearerAuth")
public class AdminProfileController {

    private final AdminProfileService adminProfileService;

    public AdminProfileController(
            AdminProfileService adminProfileService
    ) {
        this.adminProfileService = adminProfileService;
    }

    /**
     * Créer son profil administrateur.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileResponse> create(
            @Valid @RequestBody AdminProfileRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        AdminProfileResponse response =
                adminProfileService.create(
                        request,
                        user.getId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Consulter son profil.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileResponse> me(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                adminProfileService.findByUserId(
                        user.getId()
                )
        );
    }

    /**
     * Modifier son profil.
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileResponse> update(
            @Valid @RequestBody AdminProfileRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                adminProfileService.update(
                        user.getId(),
                        request
                )
        );
    }
}