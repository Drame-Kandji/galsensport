package com.example.backend.service.sport;

import com.example.backend.dto.sportif.SportifProfileRequest;
import com.example.backend.dto.sportif.SportifProfileResponse;
import com.example.backend.entity.Role;
import com.example.backend.entity.SportifProfile;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.SportifProfileRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SportifProfileServiceImpl
        implements SportifProfileService {

    private final SportifProfileRepository sportifProfileRepository;
    private final UserRepository userRepository;


    public SportifProfileServiceImpl(
            SportifProfileRepository sportifProfileRepository,
            UserRepository userRepository
    ) {
        this.sportifProfileRepository = sportifProfileRepository;
        this.userRepository = userRepository;
    }


    // =========================================================
    // CRÉER SON PROFIL SPORTIF
    // =========================================================

    @Override
    public SportifProfileResponse create(
            SportifProfileRequest request,
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );


        // Seul un USER peut avoir un profil sportif
        if (user.getRole() != Role.USER) {
            throw new ConflictException(
                    "Seul un utilisateur peut créer un profil sportif"
            );
        }


        // Un utilisateur ne peut avoir qu'un profil sportif
        if (sportifProfileRepository.existsByUser(user)) {
            throw new ConflictException(
                    "Vous possédez déjà un profil sportif"
            );
        }


        SportifProfile profile =
                new SportifProfile();

        profile.setUser(user);

        profile.setSport(
                request.getSport()
        );

        profile.setPoste(
                request.getPoste()
        );

        profile.setNiveau(
                request.getNiveau()
        );

        profile.setBio(
                request.getBio()
        );

        profile.setVille(
                request.getVille()
        );


        SportifProfile saved =
                sportifProfileRepository.save(profile);


        return toResponse(saved);
    }


    // =========================================================
    // CONSULTER SON PROFIL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SportifProfileResponse getMyProfile(
            Long userId
    ) {

        SportifProfile profile =
                sportifProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profil sportif introuvable"
                                )
                        );

        return toResponse(profile);
    }


    // =========================================================
    // MODIFIER SON PROFIL
    // =========================================================

    @Override
    public SportifProfileResponse update(
            SportifProfileRequest request,
            Long userId
    ) {

        SportifProfile profile =
                sportifProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profil sportif introuvable"
                                )
                        );


        profile.setSport(
                request.getSport()
        );

        profile.setPoste(
                request.getPoste()
        );

        profile.setNiveau(
                request.getNiveau()
        );

        profile.setBio(
                request.getBio()
        );

        profile.setVille(
                request.getVille()
        );


        return toResponse(profile);
    }


    // =========================================================
    // SUPPRIMER SON PROFIL
    // =========================================================

    @Override
    public void delete(
            Long userId
    ) {

        SportifProfile profile =
                sportifProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profil sportif introuvable"
                                )
                        );

        sportifProfileRepository.delete(profile);
    }


    // =========================================================
    // MAPPING
    // =========================================================

    private SportifProfileResponse toResponse(
            SportifProfile profile
    ) {

        User user = profile.getUser();

        return new SportifProfileResponse(
                profile.getId(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole(),
                profile.getSport(),
                profile.getPoste(),
                profile.getNiveau(),
                profile.getBio(),
                profile.getVille()
        );
    }
}