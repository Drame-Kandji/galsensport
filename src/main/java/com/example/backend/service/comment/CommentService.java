package com.example.backend.service.comment;

import com.example.backend.dto.comment.CommentRequest;
import com.example.backend.dto.comment.CommentResponse;
import com.example.backend.dto.common.PagedResponse;

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
    PagedResponse<CommentResponse> findByPost(
            Long postId,
            int page,
            int size
    );

    // Lister les réponses d'un commentaire racine.
    PagedResponse<CommentResponse> findReplies(
            Long commentId,
            int page,
            int size
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
