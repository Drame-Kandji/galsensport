package com.example.backend.dto.profile;

import com.example.backend.entity.Role;

import java.time.LocalDate;

public record UserProfileResponse(
        Long userId,
        String email,
        Role role,
        String nom,
        String prenom,
        String bio,
        String ville,
        LocalDate dateNaissance,
        String avatarUrl,
        String coverUrl,
        String sport,
        String poste,
        String niveau,
        long postsCount,
        long followersCount,
        long followingCount,
        boolean followedByMe
) {
}
