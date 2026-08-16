package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_medias")
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "post_id",
            nullable = false
    )
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private MediaType type;

    @Column(
            nullable = false,
            length = 1000
    )
    private String url;

    @Column(nullable = false)
    private Integer ordre;


    public PostMedia() {
    }


    public PostMedia(
            MediaType type,
            String url,
            Integer ordre
    ) {
        this.type = type;
        this.url = url;
        this.ordre = ordre;
    }


    public Long getId() {
        return id;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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