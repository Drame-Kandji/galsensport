package com.example.backend.service;

import com.example.backend.dto.entreprise.EntrepriseRequest;
import com.example.backend.dto.entreprise.EntrepriseResponse;
import com.example.backend.entity.Entreprise;
import com.example.backend.repository.EntrepriseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EntrepriseServiceImpl implements EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    public EntrepriseServiceImpl(
            EntrepriseRepository entrepriseRepository
    ) {
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    public EntrepriseResponse create(
            EntrepriseRequest request
    ) {

        Entreprise entreprise = new Entreprise();

        entreprise.setNomEntreprise(
                request.getNomEntreprise()
        );

        entreprise.setAdresse(
                request.getAdresse()
        );

        entreprise.setTelephone(
                request.getTelephone()
        );

        entreprise.setDescription(
                request.getDescription()
        );

        Entreprise saved =
                entrepriseRepository.save(entreprise);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepriseResponse findById(Long id) {

        Entreprise entreprise =
                entrepriseRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Entreprise introuvable"
                                )
                        );

        return toResponse(entreprise);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepriseResponse> findAll() {

        return entrepriseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EntrepriseResponse update(
            Long id,
            EntrepriseRequest request
    ) {

        Entreprise entreprise =
                entrepriseRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Entreprise introuvable"
                                )
                        );

        entreprise.setNomEntreprise(
                request.getNomEntreprise()
        );

        entreprise.setAdresse(
                request.getAdresse()
        );

        entreprise.setTelephone(
                request.getTelephone()
        );

        entreprise.setDescription(
                request.getDescription()
        );

        return toResponse(entreprise);
    }

    @Override
    public void delete(Long id) {

        if (!entrepriseRepository.existsById(id)) {
            throw new RuntimeException(
                    "Entreprise introuvable"
            );
        }

        entrepriseRepository.deleteById(id);
    }

    private EntrepriseResponse toResponse(
            Entreprise entreprise
    ) {

        return new EntrepriseResponse(
                entreprise.getId(),
                entreprise.getNomEntreprise(),
                entreprise.getAdresse(),
                entreprise.getTelephone(),
                entreprise.getDescription()
        );
    }
}