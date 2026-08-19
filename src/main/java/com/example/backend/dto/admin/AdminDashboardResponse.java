package com.example.backend.dto.admin;

/** Indicateurs agrégés affichés sur le tableau de bord administrateur. */
public record AdminDashboardResponse(
        long users,
        long enabledUsers,
        long companies,
        long posts,
        long comments
) {
}
