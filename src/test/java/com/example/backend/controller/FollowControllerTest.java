package com.example.backend.controller;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.service.follow.FollowService;
import com.example.backend.service.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FollowController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(FollowControllerTest.TestSecurityConfig.class)
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FollowService followService;

    @MockitoBean
    private UserService userService;

    private User currentUser;
    private Authentication authentication;


    // =========================================================
    // SETUP
    // =========================================================

    @BeforeEach
    void setUp() {

        currentUser = new User(
                "test@galsensport.com",
                "770000001",
                "password",
                Role.USER
        );

        setId(currentUser, 1L);

        authentication =
                new UsernamePasswordAuthenticationToken(
                        currentUser,
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );
    }


    // =========================================================
    // FOLLOW
    // =========================================================

    @Test
    void shouldFollowUser() throws Exception {

        mockMvc.perform(
                        post("/api/v1/users/2/follow")
                                .with(authentication(authentication))
                )
                .andExpect(status().isOk());

        verify(followService)
                .follow(1L, 2L);
    }


    @Test
    void shouldReturnConflictWhenAlreadyFollowing()
            throws Exception {

        doThrow(
                new ConflictException(
                        "Vous suivez déjà cet utilisateur"
                )
        )
                .when(followService)
                .follow(1L, 2L);

        mockMvc.perform(
                        post("/api/v1/users/2/follow")
                                .with(authentication(authentication))
                )
                .andExpect(status().isConflict());

        verify(followService)
                .follow(1L, 2L);
    }


    // =========================================================
    // UNFOLLOW
    // =========================================================

    @Test
    void shouldUnfollowUser() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/users/2/follow")
                                .with(authentication(authentication))
                )
                .andExpect(status().isNoContent());

        verify(followService)
                .unfollow(1L, 2L);
    }


    @Test
    void shouldReturnConflictWhenNotFollowing()
            throws Exception {

        doThrow(
                new ConflictException(
                        "Vous ne suivez pas cet utilisateur"
                )
        )
                .when(followService)
                .unfollow(1L, 2L);

        mockMvc.perform(
                        delete("/api/v1/users/2/follow")
                                .with(authentication(authentication))
                )
                .andExpect(status().isConflict());

        verify(followService)
                .unfollow(1L, 2L);
    }


    // =========================================================
    // STATUS
    // =========================================================

    @Test
    void shouldReturnFollowingStatus()
            throws Exception {

        when(
                followService.isFollowing(1L, 2L)
        ).thenReturn(true);

        mockMvc.perform(
                        get("/api/v1/users/2/follow/status")
                                .with(authentication(authentication))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(followService)
                .isFollowing(1L, 2L);
    }


    @Test
    void shouldReturnNotFollowingStatus()
            throws Exception {

        when(
                followService.isFollowing(1L, 2L)
        ).thenReturn(false);

        mockMvc.perform(
                        get("/api/v1/users/2/follow/status")
                                .with(authentication(authentication))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(followService)
                .isFollowing(1L, 2L);
    }


    // =========================================================
    // FOLLOWERS COUNT
    // =========================================================

    @Test
    void shouldReturnFollowersCount()
            throws Exception {

        when(
                followService.countFollowers(2L)
        ).thenReturn(15L);

        mockMvc.perform(
                        get("/api/v1/users/2/followers/count")
                                .with(authentication(authentication))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(followService)
                .countFollowers(2L);
    }


    // =========================================================
    // FOLLOWING COUNT
    // =========================================================

    @Test
    void shouldReturnFollowingCount()
            throws Exception {

        when(
                followService.countFollowing(1L)
        ).thenReturn(8L);

        mockMvc.perform(
                        get("/api/v1/users/1/following/count")
                                .with(authentication(authentication))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("8"));

        verify(followService)
                .countFollowing(1L);
    }


    // =========================================================
    // UTILITAIRE
    // =========================================================

    private void setId(User user, Long id) {

        try {

            Field field =
                    User.class.getDeclaredField("id");

            field.setAccessible(true);
            field.set(user, id);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


    // =========================================================
    // CONFIGURATION TEST
    // =========================================================

    @EnableMethodSecurity
    static class TestSecurityConfig {
    }
}