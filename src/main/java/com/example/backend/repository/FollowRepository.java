package com.example.backend.repository;

import com.example.backend.entity.Follow;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository
        extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(
            User follower,
            User following
    );

    void deleteByFollowerAndFollowing(
            User follower,
            User following
    );

    long countByFollowing(User following);

    long countByFollower(User follower);

    List<Follow> findByFollowingOrderByCreatedAtDesc(
            User following
    );

    List<Follow> findByFollowerOrderByCreatedAtDesc(
            User follower
    );
}