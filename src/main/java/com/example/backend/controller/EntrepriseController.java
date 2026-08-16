package com.example.backend.controller;

import com.example.backend.dto.entreprise.EntrepriseRequest;
import com.example.backend.dto.entreprise.EntrepriseResponse;
import com.example.backend.entity.User;
import com.example.backend.service.entreprise.EntrepriseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entreprises")
@SecurityRequirement(name = "bearerAuth")
public class EntrepriseController {

    private final EntrepriseService entrepriseService;

    public EntrepriseController(
            EntrepriseService entrepriseService
    ) {
        this.entrepriseService = entrepriseService;
    }

    /**
     * Voir sa propre entreprise.
     *
     * ENTREPRISE uniquement.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<EntrepriseResponse> me(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                entrepriseService.findMyEntreprise(
                        user.getId()
                )
        );
    }

    /**
     * Voir une entreprise.
     *
     * USER, ENTREPRISE et ADMIN.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntrepriseResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                entrepriseService.findById(id)
        );
    }

    /**
     * Lister toutes les entreprises.
     *
     * USER, ENTREPRISE et ADMIN.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EntrepriseResponse>> findAll() {

        return ResponseEntity.ok(
                entrepriseService.findAll()
        );
    }



    /**
     * Modifier une entreprise.
     *
     * ENTREPRISE : uniquement sa propre entreprise.
     * ADMIN : n'importe quelle entreprise.
     */
    @PutMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('ENTREPRISE')"
    )
    public ResponseEntity<EntrepriseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EntrepriseRequest request,
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        return ResponseEntity.ok(
                entrepriseService.update(
                        id,
                        request,
                        user.getId(),
                        isAdmin
                )
        );
    }

    /**
     * Supprimer une entreprise.
     *
     * ADMIN uniquement.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        entrepriseService.delete(id);

        return ResponseEntity.noContent().build();
    }
}