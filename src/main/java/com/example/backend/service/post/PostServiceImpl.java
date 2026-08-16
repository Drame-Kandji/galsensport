package com.example.backend.service.post;

import com.example.backend.dto.post.PostRequest;
import com.example.backend.dto.post.PostResponse;
import com.example.backend.dto.post.PostMediaResponse;
import com.example.backend.entity.MediaType;
import com.example.backend.entity.Post;
import com.example.backend.entity.PostMedia;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ForbiddenException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PostLikeRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    // =========================================================
    // CRÉER UN POST
    // =========================================================

    @Override
    public PostResponse create(
            PostRequest request,
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        validatePost(request);

        Post post = new Post();

        post.setContenu(request.getContenu());
        post.setAuteur(user);

        if (request.getMedias() != null) {

            for (PostRequest.PostMediaRequest mediaRequest
                    : request.getMedias()) {

                PostMedia media = new PostMedia();

                media.setType(mediaRequest.getType());
                media.setUrl(mediaRequest.getUrl());
                media.setOrdre(mediaRequest.getOrdre());

                post.addMedia(media);
            }
        }

        Post savedPost =
                postRepository.save(post);

        return toResponse(
                savedPost,
                userId
        );
    }


    // =========================================================
    // CONSULTER UN POST
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PostResponse findById(
            Long postId,
            Long currentUserId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );

        return toResponse(
                post,
                currentUserId
        );
    }


    // =========================================================
    // CONSULTER TOUS LES POSTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findAll(
            Long currentUserId
    ) {

        return postRepository.findAll()
                .stream()
                .map(post ->
                        toResponse(
                                post,
                                currentUserId
                        )
                )
                .toList();
    }


    // =========================================================
    // CONSULTER LES POSTS D'UN AUTEUR
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findByAuteur(
            Long userId,
            Long currentUserId
    ) {

        User auteur =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );

        return postRepository
                .findByAuteurOrderByCreatedAtDesc(auteur)
                .stream()
                .map(post ->
                        toResponse(
                                post,
                                currentUserId
                        )
                )
                .toList();
    }


    // =========================================================
    // MODIFIER UN POST
    // =========================================================

    @Override
    public PostResponse update(
            Long postId,
            PostRequest request,
            Long userId
    ) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post introuvable"
                        )
                );

        checkOwnership(post, userId);

        validatePost(request);

        post.setContenu(
                request.getContenu()
        );

        /*
         * On supprime les anciens médias.
         * Ils seront recréés à partir de la nouvelle requête.
         */
        post.getMedias().clear();

        if (request.getMedias() != null) {

            for (PostRequest.PostMediaRequest mediaRequest
                    : request.getMedias()) {

                PostMedia media = new PostMedia();

                media.setType(
                        mediaRequest.getType()
                );

                media.setUrl(
                        mediaRequest.getUrl()
                );

                media.setOrdre(
                        mediaRequest.getOrdre()
                );

                post.addMedia(media);
            }
        }

        return toResponse(
                post,
                userId
        );
    }


    // =========================================================
    // SUPPRIMER UN POST
    // =========================================================

    @Override
    public void delete(
            Long postId,
            Long userId
    ) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post introuvable"
                        )
                );

        checkOwnership(post, userId);

        postRepository.delete(post);
    }


    // =========================================================
    // VÉRIFIER QUE LE POST APPARTIENT À L'UTILISATEUR
    // =========================================================

    private void checkOwnership(
            Post post,
            Long userId
    ) {

        if (!post.getAuteur()
                .getId()
                .equals(userId)) {

            throw new ForbiddenException(
                    "Vous ne pouvez modifier ou supprimer que vos propres posts"
            );
        }
    }


    // =========================================================
    // VALIDATION MÉTIER DU POST
    // =========================================================

    private void validatePost(
            PostRequest request
    ) {

        boolean hasContent =
                request.getContenu() != null
                        && !request.getContenu()
                        .trim()
                        .isEmpty();

        boolean hasMedia =
                request.getMedias() != null
                        && !request.getMedias().isEmpty();

        /*
         * Un post doit contenir au minimum :
         * - du texte
         * OU
         * - au moins un média
         */
        if (!hasContent && !hasMedia) {

            throw new ConflictException(
                    "Un post doit contenir du texte ou au moins un média"
            );
        }


        if (hasMedia) {

            for (PostRequest.PostMediaRequest media
                    : request.getMedias()) {

                if (media.getType() == null) {

                    throw new ConflictException(
                            "Le type du média est obligatoire"
                    );
                }

                if (media.getUrl() == null
                        || media.getUrl()
                        .trim()
                        .isEmpty()) {

                    throw new ConflictException(
                            "L'URL du média est obligatoire"
                    );
                }

                if (media.getOrdre() == null
                        || media.getOrdre() < 0) {

                    throw new ConflictException(
                            "L'ordre du média doit être supérieur ou égal à 0"
                    );
                }
            }
        }
    }


    // =========================================================
    // MAPPING POST → RESPONSE
    // =========================================================

    private PostResponse toResponse(
            Post post,
            Long currentUserId
    ) {

        List<PostMediaResponse> medias =
                post.getMedias()
                        .stream()
                        .map(media ->
                                new PostMediaResponse(
                                        media.getId(),
                                        media.getType(),
                                        media.getUrl(),
                                        media.getOrdre()
                                )
                        )
                        .toList();


        long likesCount =
                postLikeRepository.countByPost(post);


        boolean likedByMe =
                currentUserId != null &&
                        postLikeRepository.existsByPostAndUser(
                                post,
                                userRepository.getReferenceById(
                                        currentUserId
                                )
                        );


        User auteur = post.getAuteur();


        return new PostResponse(
                post.getId(),
                post.getContenu(),

                auteur.getId(),
                auteur.getEmail(),
                auteur.getTelephone(),
                auteur.getRole(),

                medias,

                likesCount,
                likedByMe,

                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}