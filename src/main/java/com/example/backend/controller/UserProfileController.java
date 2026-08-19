package com.example.backend.controller;

import com.example.backend.dto.profile.UserProfileRequest;
import com.example.backend.dto.profile.UserProfileResponse;
import com.example.backend.entity.User;
import com.example.backend.service.profile.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userProfileService.getProfile(user.getId(), user.getId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> get(@PathVariable Long userId, Authentication authentication) {
        User viewer = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userProfileService.getProfile(userId, viewer.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> update(@Valid @RequestBody UserProfileRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userProfileService.updateMyProfile(user.getId(), request));
    }
}
