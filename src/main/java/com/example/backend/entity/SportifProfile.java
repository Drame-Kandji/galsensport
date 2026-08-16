package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "sportif_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sportif_profiles_user",
                        columnNames = "user_id"
                )
        }
)
public class SportifProfile {

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

    @Column(nullable = false, length = 100)
    private String sport;

    @Column(length = 100)
    private String poste;

    @Column(length = 50)
    private String niveau;

    @Column(length = 1000)
    private String bio;

    @Column(length = 100)
    private String ville;


    public SportifProfile() {
    }


    public SportifProfile(
            User user,
            String sport,
            String poste,
            String niveau,
            String bio,
            String ville
    ) {
        this.user = user;
        this.sport = sport;
        this.poste = poste;
        this.niveau = niveau;
        this.bio = bio;
        this.ville = ville;
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