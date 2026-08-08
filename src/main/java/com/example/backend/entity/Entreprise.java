package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "entreprises")
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(nullable = false, length = 150)
    private String nomEntreprise;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(length = 1000)
    private String description;

    public Entreprise() {
    }

    public Entreprise(
            User user,
            String nomEntreprise,
            String adresse,
            String description
    ) {
        this.user = user;
        this.nomEntreprise = nomEntreprise;
        this.adresse = adresse;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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