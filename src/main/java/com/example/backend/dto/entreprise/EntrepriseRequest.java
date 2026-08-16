package com.example.backend.dto.entreprise;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EntrepriseRequest {

    @NotBlank
    @Size(max = 150)
    private String nomEntreprise;

    @NotBlank
    @Size(max = 255)
    private String adresse;

    @Size(max = 1000)
    private String description;

    public EntrepriseRequest() {
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}