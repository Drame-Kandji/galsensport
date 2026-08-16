package com.example.backend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(
            max = 5000,
            message = "Le commentaire ne peut pas dépasser 2000 caractères"
    )
    private String contenu;


    public CommentRequest() {
    }


    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}