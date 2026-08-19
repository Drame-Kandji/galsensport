package com.example.backend.dto.post;

import com.example.backend.entity.Role;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    private Long id;

    private String contenu;

    // =========================================================
    // AUTEUR
    // =========================================================

    private Long auteurId;

    private String auteurNom;

    private String auteurPrenom;

    private String auteurEmail;

    private String auteurTelephone;

    private Role auteurRole;

    // =========================================================
    // MEDIAS
    // =========================================================

    private List<PostMediaResponse> medias;

    // =========================================================
    // LIKES
    // =========================================================

    private long likesCount;

    private boolean likedByMe;

    // =========================================================
    // REPOSTS
    // =========================================================

    private long repostsCount;

    private boolean repostedByMe;

    // =========================================================
    // DATES
    // =========================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public PostResponse() {
    }


    public PostResponse(
            Long id,
            String contenu,

            Long auteurId,
            String auteurNom,
            String auteurPrenom,
            String auteurEmail,
            String auteurTelephone,
            Role auteurRole,

            List<PostMediaResponse> medias,

            long likesCount,
            boolean likedByMe,

            long repostsCount,
            boolean repostedByMe,

            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        this.id = id;
        this.contenu = contenu;

        this.auteurId = auteurId;
        this.auteurNom = auteurNom;
        this.auteurPrenom = auteurPrenom;
        this.auteurEmail = auteurEmail;
        this.auteurTelephone = auteurTelephone;
        this.auteurRole = auteurRole;

        this.medias = medias;

        this.likesCount = likesCount;
        this.likedByMe = likedByMe;

        this.repostsCount = repostsCount;
        this.repostedByMe = repostedByMe;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Long getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public Long getAuteurId() {
        return auteurId;
    }

    public String getAuteurNom() {
        return auteurNom;
    }

    public String getAuteurPrenom() {
        return auteurPrenom;
    }

    public String getAuteurEmail() {
        return auteurEmail;
    }

    public String getAuteurTelephone() {
        return auteurTelephone;
    }

    public Role getAuteurRole() {
        return auteurRole;
    }

    public List<PostMediaResponse> getMedias() {
        return medias;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public long getRepostsCount() {
        return repostsCount;
    }

    public boolean isRepostedByMe() {
        return repostedByMe;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
