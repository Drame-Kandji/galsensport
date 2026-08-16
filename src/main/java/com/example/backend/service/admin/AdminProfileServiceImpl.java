package com.example.backend.service.admin;

import com.example.backend.dto.admin.AdminProfileRequest;
import com.example.backend.dto.admin.AdminProfileResponse;
import com.example.backend.entity.AdminProfile;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.ForbiddenException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.AdminProfileRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminProfileServiceImpl implements AdminProfileService {

    private final AdminProfileRepository adminProfileRepository;
    private final UserRepository userRepository;

    public AdminProfileServiceImpl(
            AdminProfileRepository adminProfileRepository,
            UserRepository userRepository
    ) {
        this.adminProfileRepository = adminProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AdminProfileResponse create(
            AdminProfileRequest request,
            Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException(
                    "Ce compte n'est pas un compte administrateur"
            );
        }

        if (adminProfileRepository.existsByUser(user)) {
            throw new ConflictException(
                    "Cet administrateur possède déjà un profil"
            );
        }

        AdminProfile profile = new AdminProfile();

        profile.setNom(request.getNom());
        profile.setPrenom(request.getPrenom());
        profile.setUser(user);

        AdminProfile saved =
                adminProfileRepository.save(profile);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminProfileResponse findByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        AdminProfile profile =
                adminProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profil administrateur introuvable"
                                )
                        );

        return toResponse(profile);
    }

    @Override
    public AdminProfileResponse update(
            Long userId,
            AdminProfileRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        AdminProfile profile =
                adminProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profil administrateur introuvable"
                                )
                        );

        profile.setNom(request.getNom());
        profile.setPrenom(request.getPrenom());

        return toResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminProfileResponse getMyProfile(Long userId) {

        AdminProfile profile =
                adminProfileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profil administrateur introuvable"
                                )
                        );

        return toResponse(profile);
    }



    private AdminProfileResponse toResponse(
            AdminProfile profile
    ) {

        User user = profile.getUser();

        return new AdminProfileResponse(
                profile.getId(),
                user.getEmail(),
                user.getTelephone(),
                user.getRole(),
                profile.getNom(),
                profile.getPrenom()
        );
    }
}