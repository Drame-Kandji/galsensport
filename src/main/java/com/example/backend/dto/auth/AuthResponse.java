package com.example.backend.dto.auth;


public class AuthResponse {


    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String role;

    private String token;



    public AuthResponse(
            Long id,
            String nom,
            String prenom,
            String email,
            String telephone,
            String role,
            String token
    ) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.token = token;
    }



    public Long getId() {
        return id;
    }


    public String getNom() {
        return nom;
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


    public String getToken() {
        return token;
    }
}