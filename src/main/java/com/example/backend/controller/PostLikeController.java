package com.example.backend.controller;

import com.example.backend.dto.like.LikeResponse;
import com.example.backend.entity.User;
import com.example.backend.service.post.PostLikeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@SecurityRequirement(name = "bearerAuth")
public class PostLikeController {

    private final PostLikeService postLikeService;


    public PostLikeController(
            PostLikeService postLikeService
    ) {
        this.postLikeService = postLikeService;
    }


    // =========================================================
    // LIKER UN POST
    // =========================================================

    @PostMapping("/{postId}/like")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<LikeResponse> like(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postLikeService.like(
                        postId,
                        user.getId()
                )
        );
    }


    // =========================================================
    // RETIRER SON LIKE
    // =========================================================

    @DeleteMapping("/{postId}/like")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<LikeResponse> unlike(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postLikeService.unlike(
                        postId,
                        user.getId()
                )
        );
    }


    // =========================================================
    // NOMBRE DE LIKES
    // =========================================================

    @GetMapping("/{postId}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countLikes(
            @PathVariable Long postId
    ) {

        return ResponseEntity.ok(
                postLikeService.countLikes(
                        postId
                )
        );
    }


    // =========================================================
    // L'UTILISATEUR COURANT A-T-IL LIKÉ ?
    // =========================================================

    @GetMapping("/{postId}/liked")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> hasLiked(
            @PathVariable Long postId,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postLikeService.hasLiked(
                        postId,
                        user.getId()
                )
        );
    }
}