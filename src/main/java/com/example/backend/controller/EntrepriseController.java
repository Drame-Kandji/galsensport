package com.example.backend.controller;

import com.example.backend.dto.entreprise.EntrepriseRequest;
import com.example.backend.dto.entreprise.EntrepriseResponse;
import com.example.backend.service.EntrepriseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

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
     * Créer une entreprise
     *
     * ENTREPRISE ou ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ENTREPRISE', 'ADMIN')")
    public ResponseEntity<EntrepriseResponse> create(
            @Valid @RequestBody EntrepriseRequest request
    ) {

        EntrepriseResponse response =
                entrepriseService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Consulter une entreprise.
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
     * Lister les entreprises.
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
     * ENTREPRISE ou ADMIN.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ENTREPRISE', 'ADMIN')")
    public ResponseEntity<EntrepriseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EntrepriseRequest request
    ) {

        return ResponseEntity.ok(
                entrepriseService.update(id, request)
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