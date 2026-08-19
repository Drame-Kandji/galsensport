package com.example.backend.controller;

import com.example.backend.dto.post.PostRequest;
import com.example.backend.dto.post.PostResponse;
import com.example.backend.entity.User;
import com.example.backend.service.post.PostService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.backend.dto.common.PagedResponse;

@RestController
@RequestMapping("/api/v1/posts")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;

    public PostController(
            PostService postService
    ) {
        this.postService = postService;
    }


    // =========================================================
    // CRÉER UN POST
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        PostResponse response =
                postService.create(
                        request,
                        user.getId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // LISTER TOUS LES POSTS
    // =========================================================

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PostResponse>> findAll(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postService.findAll(
                        user.getId()
                )
        );
    }

    /** Liste paginée à utiliser par le feed et les écrans à défilement infini. */
    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<PostResponse>> findPage(Authentication authentication, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(postService.findPage(user.getId(), page, size));
    }


    // =========================================================
    // MES POSTS
    // =========================================================

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<List<PostResponse>> findMyPosts(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postService.findByAuteur(
                        user.getId(),
                        user.getId()
                )
        );
    }


    // =========================================================
    // POSTS D'UN AUTEUR
    // =========================================================

    @GetMapping("/author/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PostResponse>> findByAuteur(
            @PathVariable Long userId,
            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postService.findByAuteur(
                        userId,
                        currentUser.getId()
                )
        );
    }


    // =========================================================
    // CONSULTER UN POST
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponse> findById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postService.findById(
                        id,
                        currentUser.getId()
                )
        );
    }


    // =========================================================
    // MODIFIER SON POST
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                postService.update(
                        id,
                        request,
                        user.getId()
                )
        );
    }


    // =========================================================
    // SUPPRIMER SON POST
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        postService.delete(
                id,
                user.getId()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
