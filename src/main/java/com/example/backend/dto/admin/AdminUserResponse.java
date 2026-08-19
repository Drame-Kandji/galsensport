package com.example.backend.dto.admin;

import com.example.backend.entity.Role;

/** Vue minimale, sans donnée sensible, utilisable dans la modération. */
public record AdminUserResponse(
        Long id,
        String email,
        String telephone,
        Role role,
        boolean enabled,
        boolean emailVerified
) {
}
