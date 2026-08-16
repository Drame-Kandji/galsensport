package com.example.backend.service.comment;

import com.example.backend.dto.comment.CommentRequest;
import com.example.backend.dto.comment.CommentResponse;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Post;
import com.example.backend.entity.User;
import com.example.backend.exception.ForbiddenException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl
        implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public CommentServiceImpl(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    // =========================================================
    // CRÉER UN COMMENTAIRE
    // =========================================================

    @Override
    public CommentResponse create(
            Long postId,
            CommentRequest request,
            Long userId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );


        Comment comment =
                new Comment(
                        request.getContenu(),
                        user,
                        post
                );


        Comment saved =
                commentRepository.save(comment);


        return toResponse(saved);
    }


    // =========================================================
    // CONSULTER UN COMMENTAIRE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public CommentResponse findById(
            Long commentId
    ) {

        Comment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Commentaire introuvable"
                                )
                        );


        return toResponse(comment);
    }


    // =========================================================
    // LISTER LES COMMENTAIRES D'UN POST
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> findByPost(
            Long postId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        return commentRepository
                .findByPostOrderByCreatedAtDesc(post)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================================================
    // MODIFIER SON COMMENTAIRE
    // =========================================================

    @Override
    public CommentResponse update(
            Long commentId,
            CommentRequest request,
            Long userId
    ) {

        Comment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Commentaire introuvable"
                                )
                        );


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );


        // Vérifier que le commentaire appartient
        // bien à l'utilisateur connecté.
        if (!comment.getAuteur()
                .getId()
                .equals(user.getId())) {

            throw new ForbiddenException(
                    "Vous ne pouvez modifier que vos propres commentaires"
            );
        }


        comment.setContenu(
                request.getContenu()
        );


        return toResponse(comment);
    }


    // =========================================================
    // SUPPRIMER SON COMMENTAIRE
    // =========================================================

    @Override
    public void delete(
            Long commentId,
            Long userId
    ) {

        Comment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Commentaire introuvable"
                                )
                        );


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );


        // Seul l'auteur peut supprimer son commentaire.
        if (!comment.getAuteur()
                .getId()
                .equals(user.getId())) {

            throw new ForbiddenException(
                    "Vous ne pouvez supprimer que vos propres commentaires"
            );
        }


        commentRepository.delete(comment);
    }


    // =========================================================
    // NOMBRE DE COMMENTAIRES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countByPost(
            Long postId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        return commentRepository.countByPost(post);
    }


    // =========================================================
    // MAPPING
    // =========================================================

    private CommentResponse toResponse(
            Comment comment
    ) {

        User auteur =
                comment.getAuteur();

        Post post =
                comment.getPost();


        return new CommentResponse(
                comment.getId(),
                comment.getContenu(),
                auteur.getId(),
                auteur.getEmail(),
                auteur.getTelephone(),
                auteur.getRole(),
                post.getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}