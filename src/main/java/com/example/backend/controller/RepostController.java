package com.example.backend.controller;

import com.example.backend.service.post.RepostService;
import com.example.backend.dto.repost.RepostResponse;
import com.example.backend.entity.User;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@SecurityRequirement(name = "bearerAuth")
public class RepostController {

    private final RepostService repostService;

    public RepostController(
            RepostService repostService
    ) {
        this.repostService = repostService;
    }


    // =========================================================
    // REPOST
    // =========================================================

    @PostMapping("/{postId}/repost")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<RepostResponse> repost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(repostService.repost(postId, user.getId()));
    }


    // =========================================================
    // UNREPOST
    // =========================================================

    @DeleteMapping("/{postId}/repost")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<RepostResponse> unrepost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(repostService.unrepost(postId, user.getId()));
    }


    // =========================================================
    // COUNT
    // =========================================================

    @GetMapping("/{postId}/reposts/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countReposts(
            @PathVariable Long postId
    ) {

        long count =
                repostService.countReposts(postId);

        return ResponseEntity.ok(count);
    }


    // =========================================================
    // STATUS
    // =========================================================

    @GetMapping("/{postId}/repost/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> hasReposted(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        boolean hasReposted =
                repostService.hasReposted(
                        postId,
                        user.getId()
                );

        return ResponseEntity.ok(hasReposted);
    }
}
