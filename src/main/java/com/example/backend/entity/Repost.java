package com.example.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reposts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_repost_post_user",
                        columnNames = {"post_id", "user_id"}
                )
        }
)
public class Repost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Le post qui est repartagé
     */
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "post_id",
            nullable = false
    )
    private Post post;

    /*
     * L'utilisateur qui repartage
     */
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public Repost() {
    }


    public Repost(
            Post post,
            User user
    ) {
        this.post = post;
        this.user = user;
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}