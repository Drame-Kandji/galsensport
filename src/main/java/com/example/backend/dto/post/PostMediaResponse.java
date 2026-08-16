package com.example.backend.dto.post;

import com.example.backend.entity.MediaType;

public class PostMediaResponse {

    private Long id;

    private MediaType type;

    private String url;

    private Integer ordre;


    public PostMediaResponse() {
    }


    public PostMediaResponse(
            Long id,
            MediaType type,
            String url,
            Integer ordre
    ) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.ordre = ordre;
    }


    public Long getId() {
        return id;
    }

    public MediaType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Integer getOrdre() {
        return ordre;
    }
}