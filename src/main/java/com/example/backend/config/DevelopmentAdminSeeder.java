package com.example.backend.config;

import com.example.backend.entity.AdminProfile;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.AdminProfileRepository;
import com.example.backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Crée deux comptes d'administration pour le développement local.
 * Le seeder est idempotent : il ne remplace jamais un compte existant.
 */
@Configuration
public class DevelopmentAdminSeeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevelopmentAdminSeeder.class);

    @Bean
    @ConditionalOnProperty(name = "app.seed-admins.enabled", havingValue = "true")
    CommandLineRunner seedAdministrators(
            UserRepository userRepository,
            AdminProfileRepository adminProfileRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed-admins.primary-password}") String primaryPassword,
            @Value("${app.seed-admins.secondary-password}") String secondaryPassword
    ) {
        return args -> {
            seed(userRepository, adminProfileRepository, passwordEncoder,
                    "admin@galsensport.local", "+221770000001", primaryPassword,
                    "GalsenSport", "Administrateur");
            seed(userRepository, adminProfileRepository, passwordEncoder,
                    "moderation@galsensport.local", "+221770000002", secondaryPassword,
                    "GalsenSport", "Modération");
        };
    }

    @Transactional
    void seed(
            UserRepository users,
            AdminProfileRepository profiles,
            PasswordEncoder encoder,
            String email,
            String telephone,
            String password,
            String nom,
            String prenom
    ) {
        if (users.existsByEmail(email)) {
            return;
        }

        User user = new User(email, telephone, encoder.encode(password), Role.ADMIN);
        user.setEmailVerified(true);
        User savedUser = users.save(user);
        profiles.save(new AdminProfile(nom, prenom, savedUser));
        LOGGER.info("Compte administrateur de développement créé : {}", email);
    }
}
