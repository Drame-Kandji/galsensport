package com.example.backend.repository;

import com.example.backend.entity.Entreprise;
import com.example.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository
        extends JpaRepository<Entreprise, Long> {

    Optional<Entreprise> findByUser(User user);

    Optional<Entreprise> findByUserId(Long userId);
}