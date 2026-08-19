package com.example.backend.service.post;

import com.example.backend.dto.like.LikeResponse;
import com.example.backend.entity.Post;
import com.example.backend.entity.PostLike;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PostLikeRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.notification.NotificationService;
import com.example.backend.entity.NotificationType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostLikeServiceImpl
        implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notifications;


    public PostLikeServiceImpl(
            PostLikeRepository postLikeRepository,
            PostRepository postRepository,
            UserRepository userRepository, NotificationService notifications
    ) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notifications = notifications;
    }


    // =========================================================
    // LIKER
    // =========================================================

    @Override
    public LikeResponse like(
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


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );


        if (postLikeRepository.existsByPostAndUser(
                post,
                user
        )) {

            throw new ConflictException(
                    "Vous avez déjà aimé ce post"
            );
        }


        PostLike postLike =
                new PostLike(
                        post,
                        user
                );

        postLikeRepository.save(postLike);
        notifications.notify(post.getAuteur(), user, NotificationType.LIKE, post.getId(), user.getEmail() + " a aimé votre publication.");


        long totalLikes =
                postLikeRepository.countByPost(post);


        return new LikeResponse(
                postId,
                true,
                totalLikes
        );
    }


    // =========================================================
    // RETIRER LE LIKE
    // =========================================================

    @Override
    public LikeResponse unlike(
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


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );


        if (!postLikeRepository.existsByPostAndUser(
                post,
                user
        )) {

            throw new ResourceNotFoundException(
                    "Vous n'avez pas aimé ce post"
            );
        }


        postLikeRepository.deleteByPostAndUser(
                post,
                user
        );


        long totalLikes =
                postLikeRepository.countByPost(post);


        return new LikeResponse(
                postId,
                false,
                totalLikes
        );
    }


    // =========================================================
    // NOMBRE DE LIKES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countLikes(
            Long postId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        return postLikeRepository.countByPost(post);
    }


    // =========================================================
    // L'UTILISATEUR A-T-IL LIKÉ ?
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public boolean hasLiked(
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


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Utilisateur introuvable"
                                )
                        );


        return postLikeRepository.existsByPostAndUser(
                post,
                user
        );
    }
}
