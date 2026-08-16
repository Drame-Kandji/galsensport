package com.example.backend.dto.user;

import com.example.backend.entity.Role;

public class UserResponse {

    // =========================
    // Informations communes
    // =========================

    private Long id;
    private String email;
    private String telephone;
    private Role role;

    // =========================
    // Profil USER / ADMIN
    // =========================

    private String nom;
    private String prenom;

    // =========================
    // Profil ENTREPRISE
    // =========================

    private String nomEntreprise;
    private String adresse;
    private String description;


    public UserResponse() {
    }


    // =========================
    // USER / ADMIN
    // =========================

    public UserResponse(
            Long id,
            String email,
            String telephone,
            Role role,
            String nom,
            String prenom
    ) {

        this.id = id;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
    }


    // =========================
    // ENTREPRISE
    // =========================

    public UserResponse(
            Long id,
            String email,
            String telephone,
            Role role,
            String nomEntreprise,
            String adresse,
            String description
    ) {

        this.id = id;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.nomEntreprise = nomEntreprise;
        this.adresse = adresse;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Role getRole() {
        return role;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDescription() {
        return description;
    }
}