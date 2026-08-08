package com.example.backend.dto.user;

public class UserResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;

    public UserResponse() {
    }

    public UserResponse(
            Long id,
            String nom,
            String prenom,
            String email,
            String telephone,
            String role
    ) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getRole() {
        return role;
    }
}