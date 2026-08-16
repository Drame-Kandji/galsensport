package com.example.backend.service;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;

import com.example.backend.service.jwt.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtServiceImpl jwtService;


    @BeforeEach
    void setUp() {

        jwtService = new JwtServiceImpl(
                "galsensport-secret-key-development-2026",
                86400000L
        );
    }


    @Test
    void shouldGenerateToken() {

        User user = new User(
                "aliou@gmail.com",
                "771234567",
                "password",
                Role.USER
        );


        String token =
                jwtService.generateToken(user);


        assertThat(token)
                .isNotNull()
                .isNotBlank();
    }
}