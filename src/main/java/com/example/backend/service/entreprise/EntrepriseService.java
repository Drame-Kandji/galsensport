package com.example.backend.service.entreprise;

import com.example.backend.dto.entreprise.EntrepriseRequest;
import com.example.backend.dto.entreprise.EntrepriseResponse;

import java.util.List;

public interface EntrepriseService {

    EntrepriseResponse findById(Long id);

    List<EntrepriseResponse> findAll();

    EntrepriseResponse findMyEntreprise(Long userId);

    EntrepriseResponse update(
            Long id,
            EntrepriseRequest request,
            Long currentUserId,
            boolean isAdmin
    );

    void delete(Long id);
}