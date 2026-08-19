package com.example.backend.repository;

import com.example.backend.entity.Follow;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<Follow> findByFollowingOrderByCreatedAtDesc(User following, Pageable pageable);
    Page<Follow> findByFollowerOrderByCreatedAtDesc(User follower, Pageable pageable);
}
