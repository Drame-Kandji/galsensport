package com.example.backend.service.follow;

import com.example.backend.entity.Follow;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FollowRepository;
import com.example.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowServiceImpl followService;


    private User follower;
    private User following;


    @BeforeEach
    void setUp() {

        follower = new User(
                "follower@test.com",
                "770000001",
                "password",
                Role.USER
        );

        following = new User(
                "following@test.com",
                "770000002",
                "password",
                Role.USER
        );

        // Les IDs sont nécessaires pour les tests.
        setId(follower, 1L);
        setId(following, 2L);
    }


    // =========================================================
    // FOLLOW
    // =========================================================

    @Test
    void shouldFollowUserSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(following));

        when(
                followRepository.existsByFollowerAndFollowing(
                        follower,
                        following
                )
        ).thenReturn(false);


        followService.follow(1L, 2L);


        verify(followRepository)
                .save(any(Follow.class));
    }


    @Test
    void shouldRejectSelfFollow() {

        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () -> followService.follow(1L, 1L)
                );

        assertEquals(
                "Vous ne pouvez pas vous suivre vous-même",
                exception.getMessage()
        );

        verifyNoInteractions(userRepository);
        verifyNoInteractions(followRepository);
    }


    @Test
    void shouldRejectDuplicateFollow() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(following));

        when(
                followRepository.existsByFollowerAndFollowing(
                        follower,
                        following
                )
        ).thenReturn(true);


        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () -> followService.follow(1L, 2L)
                );


        assertEquals(
                "Vous suivez déjà cet utilisateur",
                exception.getMessage()
        );

        verify(followRepository, never())
                .save(any(Follow.class));
    }


    @Test
    void shouldRejectFollowWhenFollowerDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> followService.follow(1L, 2L)
        );


        verify(followRepository, never())
                .save(any(Follow.class));
    }


    @Test
    void shouldRejectFollowWhenFollowingUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> followService.follow(1L, 2L)
        );


        verify(followRepository, never())
                .save(any(Follow.class));
    }


    // =========================================================
    // UNFOLLOW
    // =========================================================

    @Test
    void shouldUnfollowSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(following));

        when(
                followRepository.existsByFollowerAndFollowing(
                        follower,
                        following
                )
        ).thenReturn(true);


        followService.unfollow(1L, 2L);


        verify(followRepository)
                .deleteByFollowerAndFollowing(
                        follower,
                        following
                );
    }


    @Test
    void shouldRejectUnfollowWhenRelationshipDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(following));

        when(
                followRepository.existsByFollowerAndFollowing(
                        follower,
                        following
                )
        ).thenReturn(false);


        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () -> followService.unfollow(1L, 2L)
                );


        assertEquals(
                "Vous ne suivez pas cet utilisateur",
                exception.getMessage()
        );


        verify(followRepository, never())
                .deleteByFollowerAndFollowing(
                        any(User.class),
                        any(User.class)
                );
    }


    // =========================================================
    // IS FOLLOWING
    // =========================================================

    @Test
    void shouldReturnTrueWhenFollowing() {

        when(
                userRepository.getReferenceById(1L)
        ).thenReturn(follower);

        when(
                userRepository.getReferenceById(2L)
        ).thenReturn(following);

        when(
                followRepository.existsByFollowerAndFollowing(
                        follower,
                        following
                )
        ).thenReturn(true);


        boolean result =
                followService.isFollowing(1L, 2L);


        assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenNotFollowing() {

        when(
                userRepository.getReferenceById(1L)
        ).thenReturn(follower);

        when(
                userRepository.getReferenceById(2L)
        ).thenReturn(following);

        when(
                followRepository.existsByFollowerAndFollowing(
                        follower,
                        following
                )
        ).thenReturn(false);


        boolean result =
                followService.isFollowing(1L, 2L);


        assertFalse(result);
    }


    // =========================================================
    // COUNTS
    // =========================================================

    @Test
    void shouldCountFollowers() {

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(following));

        when(followRepository.countByFollowing(following))
                .thenReturn(15L);


        long result =
                followService.countFollowers(2L);


        assertEquals(15L, result);
    }


    @Test
    void shouldCountFollowing() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(followRepository.countByFollower(follower))
                .thenReturn(8L);


        long result =
                followService.countFollowing(1L);


        assertEquals(8L, result);
    }


    // =========================================================
    // FOLLOWERS
    // =========================================================

    @Test
    void shouldGetFollowers() {

        Follow follow =
                new Follow(
                        follower,
                        following
                );

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(following));

        when(
                followRepository
                        .findByFollowingOrderByCreatedAtDesc(following)
        ).thenReturn(List.of(follow));


        var result =
                followService.getFollowers(2L);


        assertEquals(1, result.size());

        assertEquals(
                follower.getId(),
                result.get(0).getId()
        );
    }


    // =========================================================
    // FOLLOWING
    // =========================================================

    @Test
    void shouldGetFollowing() {

        Follow follow =
                new Follow(
                        follower,
                        following
                );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(follower));

        when(
                followRepository
                        .findByFollowerOrderByCreatedAtDesc(follower)
        ).thenReturn(List.of(follow));


        var result =
                followService.getFollowing(1L);


        assertEquals(1, result.size());

        assertEquals(
                following.getId(),
                result.get(0).getId()
        );
    }


    // =========================================================
    // UTILITAIRE
    // =========================================================

    private void setId(User user, Long id) {

        try {

            var field =
                    User.class.getDeclaredField("id");

            field.setAccessible(true);
            field.set(user, id);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}