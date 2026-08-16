package com.example.backend.service.post;

import com.example.backend.dto.like.LikeResponse;

public interface PostLikeService {

    LikeResponse like(
            Long postId,
            Long userId
    );

    LikeResponse unlike(
            Long postId,
            Long userId
    );

    long countLikes(
            Long postId
    );

    boolean hasLiked(
            Long postId,
            Long userId
    );
}