package com.example.backend.repository;


import com.example.backend.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EntrepriseRepositoryTest {


    @Autowired
    private EntrepriseRepository entrepriseRepository;


    @Test
    void shouldSaveEntreprise(){

        User user = new User(
                "Diambars",
                "Mané",
                "contact@diambars.sn",
                "771111111",
                "password",
                Role.ENTREPRISE
        );


        Entreprise entreprise = new Entreprise(
                "Diambars FC",
                "Saly",
                "771111111",
                "Centre de formation football",
                user
        );


        Entreprise saved =
                entrepriseRepository.save(entreprise);


        assertThat(saved.getId())
                .isNotNull();
    }
}