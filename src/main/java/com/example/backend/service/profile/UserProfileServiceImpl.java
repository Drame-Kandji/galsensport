package com.example.backend.service.profile;

import com.example.backend.dto.profile.UserProfileRequest;
import com.example.backend.dto.profile.UserProfileResponse;
import com.example.backend.entity.SportifProfile;
import com.example.backend.entity.User;
import com.example.backend.entity.UserProfile;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FollowRepository;
import com.example.backend.repository.AdminProfileRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.SportifProfileRepository;
import com.example.backend.repository.UserProfileRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SportifProfileRepository sportifProfileRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final AdminProfileRepository adminProfileRepository;

    public UserProfileServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository,
                                  SportifProfileRepository sportifProfileRepository, PostRepository postRepository,
                                  FollowRepository followRepository, AdminProfileRepository adminProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.sportifProfileRepository = sportifProfileRepository;
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.adminProfileRepository = adminProfileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId, Long viewerId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> createDefaultProfile(user));
        SportifProfile sportif = sportifProfileRepository.findByUser(user).orElse(null);
        User viewer = viewerId == null ? null : userRepository.findById(viewerId).orElse(null);
        return new UserProfileResponse(user.getId(), user.getEmail(), user.getRole(), profile.getNom(), profile.getPrenom(),
                profile.getBio(), profile.getVille(), profile.getDateNaissance(), profile.getAvatarUrl(), profile.getCoverUrl(),
                sportif == null ? null : sportif.getSport(), sportif == null ? null : sportif.getPoste(),
                sportif == null ? null : sportif.getNiveau(), postRepository.countByAuteur(user),
                followRepository.countByFollowing(user), followRepository.countByFollower(user),
                viewer != null && followRepository.existsByFollowerAndFollowing(viewer, user));
    }

    @Override
    public UserProfileResponse updateMyProfile(Long userId, UserProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> createDefaultProfile(user));
        profile.setNom(request.nom()); profile.setPrenom(request.prenom()); profile.setBio(request.bio());
        profile.setVille(request.ville()); profile.setDateNaissance(request.dateNaissance());
        profile.setAvatarUrl(request.avatarUrl()); profile.setCoverUrl(request.coverUrl());
        return getProfile(userId, userId);
    }

    private UserProfile createDefaultProfile(User user) {
        String nom = adminProfileRepository.findByUser(user).map(p -> p.getNom()).orElse("GalsenSport");
        String prenom = adminProfileRepository.findByUser(user).map(p -> p.getPrenom()).orElse("Membre");
        return userProfileRepository.save(new UserProfile(user, nom, prenom));
    }
}
