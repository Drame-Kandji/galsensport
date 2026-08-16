package com.example.backend.controller;

import com.example.backend.dto.sportif.SportifProfileRequest;
import com.example.backend.dto.sportif.SportifProfileResponse;
import com.example.backend.entity.User;
import com.example.backend.service.sport.SportifProfileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sportifs/profile")
@SecurityRequirement(name = "bearerAuth")
public class SportifProfileController {

    private final SportifProfileService sportifProfileService;


    public SportifProfileController(
            SportifProfileService sportifProfileService
    ) {
        this.sportifProfileService =
                sportifProfileService;
    }


    // =========================================================
    // CRÉER SON PROFIL SPORTIF
    // =========================================================

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SportifProfileResponse> create(
            @Valid @RequestBody SportifProfileRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        SportifProfileResponse response =
                sportifProfileService.create(
                        request,
                        user.getId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // CONSULTER SON PROFIL
    // =========================================================

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SportifProfileResponse> me(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                sportifProfileService.getMyProfile(
                        user.getId()
                )
        );
    }


    // =========================================================
    // MODIFIER SON PROFIL
    // =========================================================

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SportifProfileResponse> update(
            @Valid @RequestBody SportifProfileRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                sportifProfileService.update(
                        request,
                        user.getId()
                )
        );
    }


    // =========================================================
    // SUPPRIMER SON PROFIL
    // =========================================================

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        sportifProfileService.delete(
                user.getId()
        );

        return ResponseEntity.noContent().build();
    }
}