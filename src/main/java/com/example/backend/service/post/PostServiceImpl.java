package com.example.backend.service.post;

import com.example.backend.dto.post.PostRequest;
import com.example.backend.dto.post.PostResponse;
import com.example.backend.dto.post.PostMediaResponse;

import com.example.backend.entity.Post;
import com.example.backend.entity.PostMedia;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.entity.UserProfile;
import com.example.backend.entity.Entreprise;
import com.example.backend.entity.AdminProfile;

import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ForbiddenException;
import com.example.backend.exception.ResourceNotFoundException;

import com.example.backend.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    private final UserProfileRepository userProfileRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final AdminProfileRepository adminProfileRepository;


    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository,
            UserProfileRepository userProfileRepository,
            EntrepriseRepository entrepriseRepository,
            AdminProfileRepository adminProfileRepository
    ) {

        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;

        this.userProfileRepository = userProfileRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.adminProfileRepository = adminProfileRepository;
    }


    // =========================================================
    // CRÉER UN POST
    // =========================================================

    @Override
    public PostResponse create(
            PostRequest request,
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );

        validatePost(request);

        Post post = new Post();

        post.setContenu(
                request.getContenu()
        );

        post.setAuteur(user);


        if (request.getMedias() != null) {

            for (
                    PostRequest.PostMediaRequest mediaRequest
                    : request.getMedias()
            ) {

                PostMedia media =
                        new PostMedia();

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


        Post savedPost =
                postRepository.save(post);


        return toResponse(
                savedPost,
                userId
        );
    }


    // =========================================================
    // TROUVER UN POST
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
    // TOUS LES POSTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findAll(
            Long currentUserId
    ) {

        return postRepository
                .findAllByOrderByCreatedAtDesc()
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
    // POSTS D'UN AUTEUR
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
    // MODIFIER
    // =========================================================

    @Override
    public PostResponse update(
            Long postId,
            PostRequest request,
            Long userId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        checkOwnership(
                post,
                userId
        );


        validatePost(request);


        post.setContenu(
                request.getContenu()
        );


        post.getMedias().clear();


        if (request.getMedias() != null) {

            for (
                    PostRequest.PostMediaRequest mediaRequest
                    : request.getMedias()
            ) {

                PostMedia media =
                        new PostMedia();

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
    // SUPPRIMER
    // =========================================================

    @Override
    public void delete(
            Long postId,
            Long userId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        checkOwnership(
                post,
                userId
        );


        postRepository.delete(post);
    }


    // =========================================================
    // OWNERSHIP
    // =========================================================

    private void checkOwnership(
            Post post,
            Long userId
    ) {

        if (
                !post.getAuteur()
                        .getId()
                        .equals(userId)
        ) {

            throw new ForbiddenException(
                    "Vous ne pouvez modifier ou supprimer que vos propres posts"
            );
        }
    }


    // =========================================================
    // VALIDATION
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
                        && !request.getMedias()
                        .isEmpty();


        if (!hasContent && !hasMedia) {

            throw new ConflictException(
                    "Un post doit contenir du texte ou au moins un média"
            );
        }


        if (hasMedia) {

            for (
                    PostRequest.PostMediaRequest media
                    : request.getMedias()
            ) {

                if (media.getType() == null) {

                    throw new ConflictException(
                            "Le type du média est obligatoire"
                    );
                }


                if (
                        media.getUrl() == null
                                || media.getUrl()
                                .trim()
                                .isEmpty()
                ) {

                    throw new ConflictException(
                            "L'URL du média est obligatoire"
                    );
                }


                if (
                        media.getOrdre() == null
                                || media.getOrdre() < 0
                ) {

                    throw new ConflictException(
                            "L'ordre du média doit être supérieur ou égal à 0"
                    );
                }
            }
        }
    }


    // =========================================================
    // POST → RESPONSE
    // =========================================================

    private PostResponse toResponse(
            Post post,
            Long currentUserId
    ) {

        // -----------------------------------------------------
        // MEDIAS
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // LIKES
        // -----------------------------------------------------

        long likesCount =
                postLikeRepository.countByPost(post);


        boolean likedByMe = false;


        if (currentUserId != null) {

            User currentUser =
                    userRepository.getReferenceById(
                            currentUserId
                    );


            likedByMe =
                    postLikeRepository
                            .existsByPostAndUser(
                                    post,
                                    currentUser
                            );
        }


        // -----------------------------------------------------
        // AUTEUR
        // -----------------------------------------------------

        User auteur =
                post.getAuteur();


        String auteurNom = null;

        String auteurPrenom = null;


        // -----------------------------------------------------
        // USER
        // -----------------------------------------------------

        if (auteur.getRole() == Role.USER) {

            UserProfile profile =
                    userProfileRepository
                            .findByUser(auteur)
                            .orElse(null);


            if (profile != null) {

                auteurNom =
                        profile.getNom();

                auteurPrenom =
                        profile.getPrenom();
            }
        }


        // -----------------------------------------------------
        // ENTREPRISE
        // -----------------------------------------------------

        else if (
                auteur.getRole() == Role.ENTREPRISE
        ) {

            Entreprise entreprise =
                    entrepriseRepository
                            .findByUser(auteur)
                            .orElse(null);


            if (entreprise != null) {

                auteurNom =
                        entreprise.getNomEntreprise();
            }
        }


        // -----------------------------------------------------
        // ADMIN
        // -----------------------------------------------------

        else if (
                auteur.getRole() == Role.ADMIN
        ) {

            AdminProfile profile =
                    adminProfileRepository
                            .findByUser(auteur)
                            .orElse(null);


            if (profile != null) {

                auteurNom =
                        profile.getNom();

                auteurPrenom =
                        profile.getPrenom();
            }
        }


        // -----------------------------------------------------
        // RESPONSE
        // -----------------------------------------------------

        return new PostResponse(

                post.getId(),

                post.getContenu(),

                auteur.getId(),

                auteurNom,

                auteurPrenom,

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