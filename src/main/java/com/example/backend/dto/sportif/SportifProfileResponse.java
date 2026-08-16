package com.example.backend.dto.sportif;

import com.example.backend.entity.Role;

public class SportifProfileResponse {

    private Long id;

    private String email;

    private String telephone;

    private Role role;

    private String sport;

    private String poste;

    private String niveau;

    private String bio;

    private String ville;


    public SportifProfileResponse() {
    }


    public SportifProfileResponse(
            Long id,
            String email,
            String telephone,
            Role role,
            String sport,
            String poste,
            String niveau,
            String bio,
            String ville
    ) {
        this.id = id;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.sport = sport;
        this.poste = poste;
        this.niveau = niveau;
        this.bio = bio;
        this.ville = ville;
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

    public String getSport() {
        return sport;
    }

    public String getPoste() {
        return poste;
    }

    public String getNiveau() {
        return niveau;
    }

    public String getBio() {
        return bio;
    }

    public String getVille() {
        return ville;
    }
}