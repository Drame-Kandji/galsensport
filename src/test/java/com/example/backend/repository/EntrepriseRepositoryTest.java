package com.example.backend.repository;

import com.example.backend.entity.Entreprise;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EntrepriseRepositoryTest {

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveEntreprise() {

        User user = new User(
                "aliou09@gmail.com",
                "780000009",
                "password",
                Role.USER
        );

        User savedUser = userRepository.save(user);

        Entreprise entreprise = new Entreprise(
                "Diambars FC",
                "Saly",
                "Centre de formation football",
                savedUser
        );

        Entreprise saved = entrepriseRepository.save(entreprise);

        assertThat(saved.getId()).isNotNull();
    }

}