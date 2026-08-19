package com.example.backend.service.post;

import com.example.backend.entity.Post;
import com.example.backend.entity.Repost;
import com.example.backend.entity.User;
import com.example.backend.dto.repost.RepostResponse;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.RepostRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RepostServiceImpl
        implements RepostService {

    private final RepostRepository repostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public RepostServiceImpl(
            RepostRepository repostRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.repostRepository = repostRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    // =========================================================
    // REPOST
    // =========================================================

    @Override
    public RepostResponse repost(
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


        if (repostRepository.existsByPostAndUser(
                post,
                user
        )) {

            throw new ConflictException(
                    "Vous avez déjà repartagé ce post"
            );
        }


        Repost repost =
                new Repost(
                        post,
                        user
                );


        repostRepository.save(repost);

        return new RepostResponse(
                post.getId(),
                true,
                repostRepository.countByPost(post)
        );
    }


    // =========================================================
    // RETIRER LE REPOST
    // =========================================================

    @Override
    public RepostResponse unrepost(
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


        if (!repostRepository.existsByPostAndUser(
                post,
                user
        )) {

            throw new ResourceNotFoundException(
                    "Vous n'avez pas repartagé ce post"
            );
        }


        repostRepository.deleteByPostAndUser(
                post,
                user
        );

        // flush() rend le compteur exact avant de construire la réponse HTTP.
        repostRepository.flush();
        return new RepostResponse(
                post.getId(),
                false,
                repostRepository.countByPost(post)
        );
    }


    // =========================================================
    // NOMBRE DE REPOSTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countReposts(
            Long postId
    ) {

        Post post =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Post introuvable"
                                )
                        );


        return repostRepository.countByPost(post);
    }


    // =========================================================
    // L'UTILISATEUR A-T-IL REPOSTÉ ?
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public boolean hasReposted(
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


        return repostRepository.existsByPostAndUser(
                post,
                user
        );
    }
}
