package com.example.backend.controller;

import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.service.follow.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Follow",
        description = "Gestion des abonnements entre utilisateurs"
)
public class FollowController {

    private final FollowService followService;

    public FollowController(
            FollowService followService
    ) {
        this.followService = followService;
    }

    // =========================================================
    // FOLLOW
    // =========================================================

    @Operation(
            summary = "Suivre un utilisateur",
            description = "Permet à l'utilisateur connecté de suivre un autre utilisateur."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur suivi avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Auto-follow ou utilisateur déjà suivi"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Utilisateur non authentifié"
            )
    })
    @PostMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> follow(

            @Parameter(
                    description = "ID de l'utilisateur à suivre",
                    example = "2"
            )
            @PathVariable Long userId,

            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        followService.follow(
                currentUser.getId(),
                userId
        );

        return ResponseEntity.ok().build();
    }

    // =========================================================
    // UNFOLLOW
    // =========================================================

    @Operation(
            summary = "Ne plus suivre un utilisateur",
            description = "Supprime la relation d'abonnement entre l'utilisateur connecté et l'utilisateur ciblé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Utilisateur retiré des abonnements"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "L'utilisateur ne suit pas cette personne"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Utilisateur non authentifié"
            )
    })
    @DeleteMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unfollow(

            @Parameter(
                    description = "ID de l'utilisateur à ne plus suivre",
                    example = "2"
            )
            @PathVariable Long userId,

            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        followService.unfollow(
                currentUser.getId(),
                userId
        );

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // STATUS
    // =========================================================

    @Operation(
            summary = "Vérifier le statut de suivi",
            description = "Indique si l'utilisateur connecté suit l'utilisateur ciblé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut récupéré avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Utilisateur non authentifié"
            )
    })
    @GetMapping("/{userId}/follow/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isFollowing(

            @Parameter(
                    description = "ID de l'utilisateur ciblé",
                    example = "2"
            )
            @PathVariable Long userId,

            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        boolean following =
                followService.isFollowing(
                        currentUser.getId(),
                        userId
                );

        return ResponseEntity.ok(following);
    }

    // =========================================================
    // FOLLOWERS COUNT
    // =========================================================

    @Operation(
            summary = "Compter les followers",
            description = "Retourne le nombre d'utilisateurs qui suivent l'utilisateur ciblé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre de followers récupéré"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @GetMapping("/{userId}/followers/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countFollowers(

            @Parameter(
                    description = "ID de l'utilisateur",
                    example = "2"
            )
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                followService.countFollowers(userId)
        );
    }

    // =========================================================
    // FOLLOWING COUNT
    // =========================================================

    @Operation(
            summary = "Compter les abonnements",
            description = "Retourne le nombre d'utilisateurs suivis par l'utilisateur ciblé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre d'abonnements récupéré"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @GetMapping("/{userId}/following/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countFollowing(

            @Parameter(
                    description = "ID de l'utilisateur",
                    example = "1"
            )
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                followService.countFollowing(userId)
        );
    }

    // =========================================================
    // FOLLOWERS
    // =========================================================

    @Operation(
            summary = "Récupérer les followers",
            description = "Retourne la liste des utilisateurs qui suivent l'utilisateur ciblé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @GetMapping("/{userId}/followers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponse>> getFollowers(

            @Parameter(
                    description = "ID de l'utilisateur",
                    example = "2"
            )
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                followService.getFollowers(userId)
        );
    }

    // =========================================================
    // FOLLOWING
    // =========================================================

    @Operation(
            summary = "Récupérer les abonnements",
            description = "Retourne la liste des utilisateurs suivis par l'utilisateur ciblé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @GetMapping("/{userId}/following")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponse>> getFollowing(

            @Parameter(
                    description = "ID de l'utilisateur",
                    example = "1"
            )
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                followService.getFollowing(userId)
        );
    }
}