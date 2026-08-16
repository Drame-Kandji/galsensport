package com.example.backend.repository;

import com.example.backend.entity.PostLike;
import com.example.backend.entity.Post;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository
        extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndUser(
            Post post,
            User user
    );

    void deleteByPostAndUser(
            Post post,
            User user
    );

    long countByPost(
            Post post
    );
}