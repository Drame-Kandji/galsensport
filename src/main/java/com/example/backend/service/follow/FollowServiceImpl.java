package com.example.backend.service.follow;

import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.Follow;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FollowRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.notification.NotificationService;
import com.example.backend.entity.NotificationType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notifications;

    public FollowServiceImpl(
            FollowRepository followRepository,
            UserRepository userRepository, NotificationService notifications
    ) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.notifications = notifications;
    }

    // =========================================================
    // FOLLOW
    // =========================================================

    @Override
    public void follow(
            Long followerId,
            Long followingId
    ) {

        // Empêcher l'auto-follow
        if (followerId.equals(followingId)) {
            throw new ConflictException(
                    "Vous ne pouvez pas vous suivre vous-même"
            );
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur follower introuvable"
                        )
                );

        User following = userRepository.findById(followingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur suivi introuvable"
                        )
                );

        // Vérifier si la relation existe déjà
        if (followRepository.existsByFollowerAndFollowing(
                follower,
                following
        )) {
            throw new ConflictException(
                    "Vous suivez déjà cet utilisateur"
            );
        }

        Follow follow = new Follow(
                follower,
                following
        );

        followRepository.save(follow);
        notifications.notify(following, follower, NotificationType.FOLLOW, following.getId(), follower.getEmail() + " vous suit.");
    }

    // =========================================================
    // UNFOLLOW
    // =========================================================

    @Override
    public void unfollow(
            Long followerId,
            Long followingId
    ) {

        User follower = userRepository.findById(followerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        User following = userRepository.findById(followingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur suivi introuvable"
                        )
                );

        if (!followRepository.existsByFollowerAndFollowing(
                follower,
                following
        )) {
            throw new ConflictException(
                    "Vous ne suivez pas cet utilisateur"
            );
        }

        followRepository.deleteByFollowerAndFollowing(
                follower,
                following
        );
    }

    // =========================================================
    // STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(
            Long followerId,
            Long followingId
    ) {

        User follower = userRepository.findById(followerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur follower introuvable"
                        )
                );

        User following = userRepository.findById(followingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur suivi introuvable"
                        )
                );

        return followRepository.existsByFollowerAndFollowing(
                follower,
                following
        );
    }

    // =========================================================
    // COUNT FOLLOWERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countFollowers(
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        return followRepository.countByFollowing(user);
    }

    // =========================================================
    // COUNT FOLLOWING
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countFollowing(
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        return followRepository.countByFollower(user);
    }

    // =========================================================
    // FOLLOWERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getFollowers(
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        return followRepository
                .findByFollowingOrderByCreatedAtDesc(user)
                .stream()
                .map(follow ->
                        toUserResponse(
                                follow.getFollower()
                        )
                )
                .toList();
    }

    // =========================================================
    // FOLLOWING
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getFollowing(
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        return followRepository
                .findByFollowerOrderByCreatedAtDesc(user)
                .stream()
                .map(follow ->
                        toUserResponse(
                                follow.getFollowing()
                        )
                )
                .toList();
    }

    // =========================================================
    // USER → USER RESPONSE
    // =========================================================

    private UserResponse toUserResponse(
            User user
    ) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole(),
                null,
                null
        );
    }
}
