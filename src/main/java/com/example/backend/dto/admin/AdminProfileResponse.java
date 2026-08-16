package com.example.backend.dto.admin;

import com.example.backend.entity.Role;

public class AdminProfileResponse {

    private Long id;

    private String email;

    private String telephone;

    private Role role;

    private String nom;

    private String prenom;


    public AdminProfileResponse() {
    }


    public AdminProfileResponse(
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
}