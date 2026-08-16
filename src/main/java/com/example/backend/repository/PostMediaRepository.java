package com.example.backend.repository;

import com.example.backend.entity.PostMedia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostMediaRepository
        extends JpaRepository<PostMedia, Long> {

    List<PostMedia> findByPostIdOrderByOrdreAsc(Long postId);
}