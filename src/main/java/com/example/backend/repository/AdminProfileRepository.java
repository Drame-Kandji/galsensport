package com.example.backend.repository;

import com.example.backend.entity.AdminProfile;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminProfileRepository
        extends JpaRepository<AdminProfile, Long> {

    Optional<AdminProfile> findByUser(User user);

    Optional<AdminProfile> findByUserId(Long userId);

    boolean existsByUser(User user);
}