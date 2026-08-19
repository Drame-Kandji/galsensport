package com.example.backend.repository;

import com.example.backend.entity.Post;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository
        extends JpaRepository<Post, Long> {

    List<Post> findByAuteurOrderByCreatedAtDesc(User auteur);
    List<Post> findAllByOrderByCreatedAtDesc();

    long countByAuteur(User auteur);
}
