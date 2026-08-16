package com.example.backend.controller;

import com.example.backend.service.post.RepostService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
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
    public ResponseEntity<Void> repost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {

        repostService.repost(
                postId,
                userId
        );

        return ResponseEntity.ok().build();
    }


    // =========================================================
    // UNREPOST
    // =========================================================

    @DeleteMapping("/{postId}/repost")
    public ResponseEntity<Void> unrepost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {

        repostService.unrepost(
                postId,
                userId
        );

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // COUNT
    // =========================================================

    @GetMapping("/{postId}/reposts/count")
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
    public ResponseEntity<Boolean> hasReposted(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {

        boolean hasReposted =
                repostService.hasReposted(
                        postId,
                        userId
                );

        return ResponseEntity.ok(hasReposted);
    }
}