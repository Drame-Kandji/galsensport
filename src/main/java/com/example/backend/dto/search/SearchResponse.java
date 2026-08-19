package com.example.backend.dto.search;

import java.util.List;

/** Résultats regroupés, limités et indépendants des entités JPA. */
public record SearchResponse(List<SearchUserResponse> users, List<SearchCompanyResponse> companies, List<SearchPostResponse> posts) {
    public record SearchUserResponse(Long id, String email, String telephone) {}
    public record SearchCompanyResponse(Long userId, String name, String description) {}
    public record SearchPostResponse(Long id, String content, Long authorId, String authorEmail) {}
}
