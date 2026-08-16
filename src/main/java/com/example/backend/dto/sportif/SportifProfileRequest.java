package com.example.backend.dto.sportif;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SportifProfileRequest {

    @NotBlank(message = "Le sport est obligatoire")
    @Size(max = 200, message = "Le sport ne peut pas dépasser 200 caractères")
    private String sport;

    @Size(max = 200, message = "Le poste ne peut pas dépasser 200 caractères")
    private String poste;

    @Size(max = 100, message = "Le niveau ne peut pas dépasser 100 caractères")
    private String niveau;

    @Size(max = 2000, message = "La bio ne peut pas dépasser 2000 caractères")
    private String bio;

    @Size(max = 200, message = "La ville ne peut pas dépasser 200 caractères")
    private String ville;


    public SportifProfileRequest() {
    }


    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }


    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }


    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}