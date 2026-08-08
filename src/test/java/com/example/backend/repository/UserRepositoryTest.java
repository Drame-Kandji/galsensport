package com.example.backend.repository;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldSaveUser(){

        User user = new User(
                "Sadio",
                "Mané",
                "sadio@gmail.com",
                "771234567",
                "password",
                Role.USER
        );


        User savedUser = userRepository.save(user);


        assertThat(savedUser.getId()).isNotNull();
    }



    @Test
    void shouldFindUserByEmail(){

        User user = new User(
                "Mamadou",
                "Mané",
                "mamadou@gmail.com",
                "771111111",
                "password",
                Role.USER
        );


        userRepository.save(user);


        User found = userRepository
                .findByEmail("mamadou@gmail.com")
                .orElse(null);


        assertThat(found).isNotNull();
        assertThat(found.getNom()).isEqualTo("Mamadou");

    }

}