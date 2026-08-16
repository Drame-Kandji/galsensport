package com.example.backend.controller;

import com.example.backend.service.post.RepostService;
import com.example.backend.service.user.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepostController.class)
@AutoConfigureMockMvc(addFilters = false)
class RepostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepostService repostService;

    @MockitoBean
    private UserService userService;


    // =========================================================
    // POST /api/posts/{postId}/repost
    // =========================================================

    @Test
    void shouldRepostPost() throws Exception {

        mockMvc.perform(
                        post("/api/posts/1/repost")
                                .param("userId", "2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(repostService)
                .repost(1L, 2L);
    }


    // =========================================================
    // DELETE /api/posts/{postId}/repost
    // =========================================================

    @Test
    void shouldUnrepostPost() throws Exception {

        mockMvc.perform(
                        delete("/api/posts/1/repost")
                                .param("userId", "2")
                )
                .andExpect(status().isNoContent());

        verify(repostService)
                .unrepost(1L, 2L);
    }


    // =========================================================
    // GET COUNT
    // =========================================================

    @Test
    void shouldCountReposts() throws Exception {

        when(repostService.countReposts(1L))
                .thenReturn(5L);

        mockMvc.perform(
                        get("/api/posts/1/reposts/count")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(repostService)
                .countReposts(1L);
    }


    // =========================================================
    // GET STATUS
    // =========================================================

    @Test
    void shouldReturnRepostStatus() throws Exception {

        when(repostService.hasReposted(1L, 2L))
                .thenReturn(true);

        mockMvc.perform(
                        get("/api/posts/1/repost/status")
                                .param("userId", "2")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(repostService)
                .hasReposted(1L, 2L);
    }
}