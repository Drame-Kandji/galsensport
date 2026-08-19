package com.example.backend.dto.comment;

import com.example.backend.entity.Role;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;

    private String contenu;

    private Long auteurId;

    private String auteurEmail;

    private String auteurTelephone;

    private Role auteurRole;

    private Long postId;

    private Long parentId;

    private long repliesCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public CommentResponse() {
    }


    public CommentResponse(
            Long id,
            String contenu,
            Long auteurId,
            String auteurEmail,
            String auteurTelephone,
            Role auteurRole,
            Long postId,
            Long parentId,
            long repliesCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.contenu = contenu;
        this.auteurId = auteurId;
        this.auteurEmail = auteurEmail;
        this.auteurTelephone = auteurTelephone;
        this.auteurRole = auteurRole;
        this.postId = postId;
        this.parentId = parentId;
        this.repliesCount = repliesCount;
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

    public String getAuteurEmail() {
        return auteurEmail;
    }

    public String getAuteurTelephone() {
        return auteurTelephone;
    }

    public Role getAuteurRole() {
        return auteurRole;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public long getRepliesCount() {
        return repliesCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
