package com.example.backend.service;

import com.example.backend.dto.entreprise.EntrepriseRequest;
import com.example.backend.dto.entreprise.EntrepriseResponse;

import java.util.List;

public interface EntrepriseService {

    EntrepriseResponse create(
            EntrepriseRequest request
    );

    EntrepriseResponse findById(Long id);

    List<EntrepriseResponse> findAll();

    EntrepriseResponse update(
            Long id,
            EntrepriseRequest request
    );

    void delete(Long id);
}