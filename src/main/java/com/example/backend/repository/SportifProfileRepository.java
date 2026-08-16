package com.example.backend.repository;

import com.example.backend.entity.SportifProfile;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportifProfileRepository
        extends JpaRepository<SportifProfile, Long> {

    Optional<SportifProfile> findByUser(User user);

    Optional<SportifProfile> findByUserId(Long userId);

    boolean existsByUser(User user);

    boolean existsByUserId(Long userId);
}