package com.example.backend.repository;

import com.example.backend.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository
        extends JpaRepository<Entreprise, Long> {
}