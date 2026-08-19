package com.example.backend.service.post;

import com.example.backend.dto.repost.RepostResponse;

public interface RepostService {

    RepostResponse repost(
            Long postId,
            Long userId
    );

    RepostResponse unrepost(
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
