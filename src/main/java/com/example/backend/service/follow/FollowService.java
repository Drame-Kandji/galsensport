package com.example.backend.service.follow;

import com.example.backend.dto.user.UserResponse;

import java.util.List;

public interface FollowService {

    void follow(
            Long followerId,
            Long followingId
    );

    void unfollow(
            Long followerId,
            Long followingId
    );

    boolean isFollowing(
            Long followerId,
            Long followingId
    );

    long countFollowers(
            Long userId
    );

    long countFollowing(
            Long userId
    );

    List<UserResponse> getFollowers(
            Long userId
    );

    List<UserResponse> getFollowing(
            Long userId
    );
}