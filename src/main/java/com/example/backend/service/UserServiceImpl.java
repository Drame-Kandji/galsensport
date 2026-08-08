package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        if (userRepository.existsByTelephone(user.getTelephone())) {
            throw new RuntimeException("Téléphone déjà utilisé");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Utilisateur introuvable"
                        )
                );
    }

    @Override
    public User findByTelephone(String telephone) {

        return userRepository
                .findByTelephone(telephone)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Utilisateur introuvable"
                        )
                );
    }

    @Override
    public User login(String login, String password) {

        User user;

        if (login.contains("@")) {

            user = userRepository
                    .findByEmail(login)
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Identifiants incorrects"
                            )
                    );

        } else {

            user = userRepository
                    .findByTelephone(login)
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Identifiants incorrects"
                            )
                    );
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Identifiants incorrects");
        }

        return user;
    }}