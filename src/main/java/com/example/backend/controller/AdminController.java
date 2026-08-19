package com.example.backend.controller;

import com.example.backend.dto.admin.AdminDashboardResponse;
import com.example.backend.dto.admin.AdminUserResponse;
import com.example.backend.dto.common.PagedResponse;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.exception.ForbiddenException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.EntrepriseRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Endpoints de supervision réservés aux administrateurs. */
@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository users;
    private final PostRepository posts;
    private final CommentRepository comments;
    private final EntrepriseRepository entreprises;

    public AdminController(UserRepository users, PostRepository posts, CommentRepository comments, EntrepriseRepository entreprises) {
        this.users = users; this.posts = posts; this.comments = comments; this.entreprises = entreprises;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> dashboard() {
        return ResponseEntity.ok(new AdminDashboardResponse(users.count(), users.findAll().stream().filter(User::isEnabled).count(), entreprises.count(), posts.count(), comments.count()));
    }

    @GetMapping("/users")
    public ResponseEntity<PagedResponse<AdminUserResponse>> listUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PagedResponse.from(users.findAll(PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50))), this::toResponse));
    }

    @PutMapping("/users/{userId}/enabled")
    public ResponseEntity<AdminUserResponse> setEnabled(@PathVariable Long userId, @RequestParam boolean value, Authentication authentication) {
        User target = users.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        User current = (User) authentication.getPrincipal();
        // Un administrateur ne peut pas se verrouiller lui-même depuis cette interface.
        if (target.getId().equals(current.getId())) throw new ForbiddenException("Vous ne pouvez pas désactiver votre propre compte");
        target.setEnabled(value);
        return ResponseEntity.ok(toResponse(users.save(target)));
    }

    private AdminUserResponse toResponse(User user) {
        return new AdminUserResponse(user.getId(), user.getEmail(), user.getTelephone(), user.getRole(), user.isEnabled(), user.isEmailVerified());
    }
}
