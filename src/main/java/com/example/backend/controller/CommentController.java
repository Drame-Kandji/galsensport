package com.example.backend.controller;

import com.example.backend.dto.comment.CommentRequest;
import com.example.backend.dto.comment.CommentResponse;
import com.example.backend.entity.User;
import com.example.backend.service.comment.CommentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;


    public CommentController(
            CommentService commentService
    ) {
        this.commentService = commentService;
    }


    // =========================================================
    // CRÉER UN COMMENTAIRE
    // =========================================================

    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();


        CommentResponse response =
                commentService.create(
                        postId,
                        request,
                        user.getId()
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // LISTER LES COMMENTAIRES D'UN POST
    // =========================================================

    @GetMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentResponse>> findByPost(
            @PathVariable Long postId
    ) {

        return ResponseEntity.ok(
                commentService.findByPost(postId)
        );
    }


    // =========================================================
    // NOMBRE DE COMMENTAIRES
    // =========================================================

    @GetMapping("/posts/{postId}/comments/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> count(
            @PathVariable Long postId
    ) {

        return ResponseEntity.ok(
                commentService.countByPost(postId)
        );
    }


    // =========================================================
    // CONSULTER UN COMMENTAIRE
    // =========================================================

    @GetMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> findById(
            @PathVariable Long commentId
    ) {

        return ResponseEntity.ok(
                commentService.findById(commentId)
        );
    }


    // =========================================================
    // MODIFIER SON COMMENTAIRE
    // =========================================================

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();


        return ResponseEntity.ok(
                commentService.update(
                        commentId,
                        request,
                        user.getId()
                )
        );
    }


    // =========================================================
    // SUPPRIMER SON COMMENTAIRE
    // =========================================================

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();


        commentService.delete(
                commentId,
                user.getId()
        );


        return ResponseEntity
                .noContent()
                .build();
    }
}