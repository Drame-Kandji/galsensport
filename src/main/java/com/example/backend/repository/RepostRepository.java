package com.example.backend.repository;

import com.example.backend.entity.Post;
import com.example.backend.entity.Repost;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepostRepository
        extends JpaRepository<Repost, Long> {

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

    List<Repost> findByPostOrderByCreatedAtDesc(
            Post post
    );
}