package com.example.backend.repository;

import com.example.backend.entity.Comment;
import com.example.backend.entity.Post;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    /**
     * Tous les commentaires d'un post,
     * du plus récent au plus ancien.
     */
    List<Comment> findByPostOrderByCreatedAtDesc(
            Post post
    );

    Page<Comment> findByPostAndParentIsNullOrderByCreatedAtDesc(
            Post post,
            Pageable pageable
    );

    Page<Comment> findByParentOrderByCreatedAtAsc(
            Comment parent,
            Pageable pageable
    );

    long countByParent(Comment parent);

    /**
     * Tous les commentaires d'un utilisateur.
     */
    List<Comment> findByAuteurOrderByCreatedAtDesc(
            User auteur
    );

    /**
     * Vérifier si un commentaire appartient
     * à un utilisateur.
     */
    boolean existsByIdAndAuteur(
            Long id,
            User auteur
    );

    /**
     * Compter les commentaires d'un post.
     */
    long countByPost(
            Post post
    );
}
