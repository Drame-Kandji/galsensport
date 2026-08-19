package com.example.backend.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserProfileRequest(
        @NotBlank @Size(max = 120) String nom,
        @NotBlank @Size(max = 120) String prenom,
        @Size(max = 2000) String bio,
        @Size(max = 120) String ville,
        @Past LocalDate dateNaissance,
        @Size(max = 1000) String avatarUrl,
        @Size(max = 1000) String coverUrl
) {
}
