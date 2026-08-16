package com.example.backend.service.post;

public interface RepostService {

    void repost(
            Long postId,
            Long userId
    );

    void unrepost(
            Long postId,
            Long userId
    );

    long countReposts(
            Long postId
    );

    boolean hasReposted(
            Long postId,
            Long userId
    );
}