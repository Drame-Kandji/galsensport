package com.example.backend.dto.entreprise;

import com.example.backend.entity.Role;

public class EntrepriseResponse {

    private Long id;

    private String email;

    private String telephone;

    private Role role;

    private String nomEntreprise;

    private String adresse;

    private String description;


    public EntrepriseResponse() {
    }


    public EntrepriseResponse(
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