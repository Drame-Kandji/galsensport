package com.example.backend.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class RegisterRequest {


    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le nom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;


    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;


    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(
            min = 8,
            message = "Le mot de passe doit contenir au minimum 8 caractères"
    )
    private String password;


    public RegisterRequest() {
    }


    public RegisterRequest(
            String nom,
            String prenom,
            String email,
            String telephone,
            String password
    ) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }


    public String getNom() {
        return nom;
    }


    public String getPrenom() {
        return nom;
    }


    public String getEmail() {
        return email;
    }


    public String getTelephone() {
        return telephone;
    }


    public String getPassword() {
        return password;
    }
}