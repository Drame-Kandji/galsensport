package com.example.backend.dto.like;

public class LikeResponse {

    private Long postId;

    private boolean liked;

    private long totalLikes;


    public LikeResponse() {
    }


    public LikeResponse(
            Long postId,
            boolean liked,
            long totalLikes
    ) {
        this.postId = postId;
        this.liked = liked;
        this.totalLikes = totalLikes;
    }


    public Long getPostId() {
        return postId;
    }

    public boolean isLiked() {
        return liked;
    }

    public long getTotalLikes() {
        return totalLikes;
    }
}