package com.example.backend.service.comment;

import com.example.backend.dto.comment.CommentRequest;
import com.example.backend.dto.comment.CommentResponse;

import java.util.List;

public interface CommentService {

    // Créer un commentaire
    CommentResponse create(
            Long postId,
            CommentRequest request,
            Long userId
    );

    // Consulter un commentaire
    CommentResponse findById(
            Long commentId
    );

    // Lister les commentaires d'un post
    List<CommentResponse> findByPost(
            Long postId
    );

    // Modifier son commentaire
    CommentResponse update(
            Long commentId,
            CommentRequest request,
            Long userId
    );

    // Supprimer son commentaire
    void delete(
            Long commentId,
            Long userId
    );

    // Nombre de commentaires d'un post
    long countByPost(
            Long postId
    );
}