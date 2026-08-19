package com.example.backend.repository;

import com.example.backend.entity.Post;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository
        extends JpaRepository<Post, Long> {

    List<Post> findByAuteurOrderByCreatedAtDesc(User auteur);
    List<Post> findAllByOrderByCreatedAtDesc();
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByAuteur(User auteur);
}
