package com.example.backend.dto.repost;

/** État de repartage d'un post pour l'utilisateur connecté. */
public record RepostResponse(
        Long postId,
        boolean reposted,
        long totalReposts
) {
}
