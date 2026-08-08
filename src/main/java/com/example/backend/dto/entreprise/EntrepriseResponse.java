package com.example.backend.dto.entreprise;

public class EntrepriseResponse {

    private Long id;
    private String nomEntreprise;
    private String adresse;
    private String telephone;
    private String description;

    public EntrepriseResponse() {
    }

    public EntrepriseResponse(
            Long id,
            String nomEntreprise,
            String adresse,
            String telephone,
            String description
    ) {
        this.id = id;
        this.nomEntreprise = nomEntreprise;
        this.adresse = adresse;
        this.telephone = telephone;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getDescription() {
        return description;
    }
}