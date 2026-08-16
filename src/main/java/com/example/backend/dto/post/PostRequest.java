package com.example.backend.dto.post;

import com.example.backend.entity.MediaType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class PostRequest {

    @Size(
            max = 8000,
            message = "Le contenu ne peut pas dépasser 5000 caractères"
    )
    private String contenu;

    @Valid
    private List<PostMediaRequest> medias = new ArrayList<>();


    public PostRequest() {
    }


    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }


    public List<PostMediaRequest> getMedias() {
        return medias;
    }

    public void setMedias(List<PostMediaRequest> medias) {
        this.medias = medias;
    }


    public static class PostMediaRequest {

        private MediaType type;

        private String url;

        private Integer ordre;


        public PostMediaRequest() {
        }


        public MediaType getType() {
            return type;
        }

        public void setType(MediaType type) {
            this.type = type;
        }


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


        public Integer getOrdre() {
            return ordre;
        }

        public void setOrdre(Integer ordre) {
            this.ordre = ordre;
        }
    }
}