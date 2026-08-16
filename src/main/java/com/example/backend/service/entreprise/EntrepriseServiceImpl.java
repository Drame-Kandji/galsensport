package com.example.backend.service.entreprise;

import com.example.backend.dto.entreprise.EntrepriseRequest;
import com.example.backend.dto.entreprise.EntrepriseResponse;
import com.example.backend.entity.Entreprise;
import com.example.backend.repository.EntrepriseRepository;
import com.example.backend.exception.ForbiddenException;
import com.example.backend.exception.ResourceNotFoundException;

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
    @Transactional(readOnly = true)
    public EntrepriseResponse findById(Long id) {

        Entreprise entreprise =
                entrepriseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
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
    @Transactional(readOnly = true)
    public EntrepriseResponse findMyEntreprise(Long userId) {

        Entreprise entreprise =
                entrepriseRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Entreprise introuvable"
                                )
                        );

        return toResponse(entreprise);
    }

    @Override
    public EntrepriseResponse update(
            Long id,
            EntrepriseRequest request,
            Long currentUserId,
            boolean isAdmin
    ) {

        Entreprise entreprise =
                entrepriseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Entreprise introuvable"
                                )
                        );

        /*
         * Un ADMIN peut modifier n'importe quelle entreprise.
         *
         * Une ENTREPRISE ne peut modifier
         * que sa propre entreprise.
         */
        if (!isAdmin &&
                !entreprise.getUser().getId().equals(currentUserId)) {

            throw new ForbiddenException(
                    "Vous ne pouvez modifier que votre propre entreprise"
            );
        }
        entreprise.setNomEntreprise(
                request.getNomEntreprise()
        );

        entreprise.setAdresse(
                request.getAdresse()
        );



        entreprise.setDescription(
                request.getDescription()
        );

        return toResponse(entreprise);
    }

    @Override
    public void delete(Long id) {

        if (!entrepriseRepository.existsById(id)) {

            throw new ResourceNotFoundException(
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

                entreprise.getUser().getEmail(),

                entreprise.getUser().getTelephone(),

                entreprise.getUser().getRole(),

                entreprise.getNomEntreprise(),

                entreprise.getAdresse(),

                entreprise.getDescription()
        );
    }
}