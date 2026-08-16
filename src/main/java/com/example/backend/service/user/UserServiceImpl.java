package com.example.backend.service.user;

import com.example.backend.dto.auth.RegisterCompanyRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.*;
import com.example.backend.exception.ConflictException;
import com.example.backend.exception.InvalidCredentialsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.AdminProfileRepository;
import com.example.backend.repository.EntrepriseRepository;
import com.example.backend.repository.UserProfileRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.dto.auth.RegisterUserRequest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            AdminProfileRepository adminProfileRepository,
            EntrepriseRepository entrepriseRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.adminProfileRepository = adminProfileRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================================================
    // INSCRIPTION USER
    // =========================================================

    @Override
    public User createUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "Cette adresse email est déjà utilisée"
            );
        }

        if (userRepository.existsByTelephone(request.getTelephone())) {
            throw new ConflictException(
                    "Ce numéro de téléphone est déjà utilisé"
            );
        }

        User user = new User();

        user.setEmail(request.getEmail());

        user.setTelephone(request.getTelephone());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Le rôle est imposé par le backend
        user.setRole(Role.USER);

        User savedUser =
                userRepository.save(user);

        UserProfile profile = new UserProfile();

        profile.setUser(savedUser);
        profile.setNom(request.getNom());
        profile.setPrenom(request.getPrenom());

        userProfileRepository.save(profile);

        return savedUser;
    }


    // =========================================================
    // VÉRIFICATION EMAIL / TÉLÉPHONE
    // =========================================================

    private void checkEmailAndTelephone(
            String email,
            String telephone
    ) {

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(
                    "Cette adresse email est déjà utilisée"
            );
        }

        if (userRepository.existsByTelephone(telephone)) {
            throw new ConflictException(
                    "Ce numéro de téléphone est déjà utilisé"
            );
        }
    }


    // =========================================================
    // RECHERCHE PAR EMAIL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    // =========================================================
    // RECHERCHE PAR TÉLÉPHONE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public User findByTelephone(String telephone) {

        return userRepository
                .findByTelephone(telephone)
                .orElse(null);
    }

    // =========================================================
    // CONNEXION
    // =========================================================

    @Override
    public User login(
            String login,
            String password
    ) {

        User user = findByEmail(login);

        if (user == null) {
            user = findByTelephone(login);
        }

        if (user == null) {
            throw new InvalidCredentialsException(
                    "Email ou téléphone incorrect"
            );
        }

        if (!passwordEncoder.matches(
                password,
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Mot de passe incorrect"
            );
        }

        return user;
    }

    // =========================================================
    // UTILISATEUR CONNECTÉ
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserResponse(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );


        // =========================
        // USER
        // =========================

        if (user.getRole() == Role.USER) {

            UserProfile profile =
                    userProfileRepository.findByUser(user)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Profil utilisateur introuvable"
                                    )
                            );

            return new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getTelephone(),
                    user.getRole(),
                    profile.getNom(),
                    profile.getPrenom()
            );
        }


        // =========================
        // ENTREPRISE
        // =========================

        if (user.getRole() == Role.ENTREPRISE) {

            Entreprise entreprise =
                    entrepriseRepository.findByUser(user)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Profil entreprise introuvable")
                            );

            return new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getTelephone(),
                    user.getRole(),
                    entreprise.getNomEntreprise(),
                    entreprise.getAdresse(),
                    entreprise.getDescription()
            );
        }


        // =========================
        // ADMIN
        // =========================

        if (user.getRole() == Role.ADMIN) {

            AdminProfile profile =
                    adminProfileRepository.findByUser(user)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Profil administrateur introuvable"
                                    )
                            );

            return new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getTelephone(),
                    user.getRole(),
                    profile.getNom(),
                    profile.getPrenom()
            );
        }


        throw new ResourceNotFoundException(
                "Rôle utilisateur non supporté"
        );
    }


    @Override
    public User createCompany(RegisterCompanyRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Cette adresse email est déjà utilisée"
            );
        }

        if (userRepository.existsByTelephone(request.getTelephone())) {
            throw new RuntimeException(
                    "Ce numéro de téléphone est déjà utilisé"
            );
        }

        User user = new User();

        user.setEmail(request.getEmail());

        user.setTelephone(request.getTelephone());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Le rôle est imposé par le backend
        user.setRole(Role.ENTREPRISE);

        User savedUser =
                userRepository.save(user);

        Entreprise entreprise = new Entreprise();

        entreprise.setUser(savedUser);

        entreprise.setNomEntreprise(
                request.getNomEntreprise()
        );

        entreprise.setAdresse(
                request.getAdresse()
        );

        entreprise.setDescription(
                request.getDescription()
        );

        entrepriseRepository.save(entreprise);

        return savedUser;
    }
}