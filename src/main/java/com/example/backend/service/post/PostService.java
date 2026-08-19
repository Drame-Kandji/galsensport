package com.example.backend.service.post;

import com.example.backend.dto.post.PostRequest;
import com.example.backend.dto.post.PostResponse;
import com.example.backend.dto.common.PagedResponse;

import java.util.List;

public interface PostService {

    PostResponse create(
            PostRequest request,
            Long userId
    );

    PostResponse findById(
            Long postId,
            Long currentUserId
    );

    List<PostResponse> findAll(
            Long currentUserId
    );

    PagedResponse<PostResponse> findPage(Long currentUserId, int page, int size);

    List<PostResponse> findByAuteur(
            Long userId,
            Long currentUserId
    );

    PostResponse update(
            Long postId,
            PostRequest request,
            Long userId
    );

    void delete(
            Long postId,
            Long userId
    );
}
