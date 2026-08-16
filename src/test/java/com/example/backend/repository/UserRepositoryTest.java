package com.example.backend.repository;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldSaveUser() {

        User user = new User(
                "aliou09@gmail.com",
                "780000009",
                "password",
                Role.USER
        );

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId())
                .isNotNull();

        assertThat(savedUser.getEmail())
                .isEqualTo("aliou09@gmail.com");

        assertThat(savedUser.getTelephone())
                .isEqualTo("780000009");

        assertThat(savedUser.getRole())
                .isEqualTo(Role.USER);
    }


    @Test
    void shouldFindUserByEmail() {

        User user = new User(
                "mamadou@gmail.com",
                "771234568",
                "password",
                Role.USER
        );

        userRepository.save(user);

        User found = userRepository
                .findByEmail("mamadou@gmail.com")
                .orElse(null);

        assertThat(found)
                .isNotNull();

        assertThat(found.getEmail())
                .isEqualTo("mamadou@gmail.com");

        assertThat(found.getTelephone())
                .isEqualTo("771234568");

        assertThat(found.getRole())
                .isEqualTo(Role.USER);
    }
}