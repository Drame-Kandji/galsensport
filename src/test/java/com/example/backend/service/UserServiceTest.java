package com.example.backend.service;


import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;


    /*
        Test 1 :
        Création utilisateur
    */
    @Test
    void shouldCreateUser(){


        User user = new User(
                "Sadio",
                "Mané",
                "sadio@gmail.com",
                "771234567",
                "password",
                Role.USER
        );


        when(userRepository.existsByEmail(user.getEmail()))
                .thenReturn(false);


        when(userRepository.existsByTelephone(user.getTelephone()))
                .thenReturn(false);


        when(userRepository.save(user))
                .thenReturn(user);

        when(passwordEncoder.encode("password"))
                .thenReturn("password-hashe");

        User savedUser = userService.createUser(user);

        assertThat(user.getPassword())
                .isEqualTo("password-hashe");

        verify(passwordEncoder)
                .encode("password");

        assertThat(savedUser)
                .isNotNull();


        assertThat(savedUser.getEmail())
                .isEqualTo("sadio@gmail.com");


        verify(userRepository)
                .save(user);

    }





    /*
        Test 2 :
        Email déjà utilisé
    */
    @Test
    void shouldNotCreateUserWithExistingEmail(){


        User user = new User(
                "Mamadou",
                "Mané",
                "mamadou@gmail.com",
                "771111111",
                "password",
                Role.USER
        );



        when(userRepository.existsByEmail(user.getEmail()))
                .thenReturn(true);



        assertThrows(RuntimeException.class,
                () -> userService.createUser(user));



        verify(userRepository, never())
                .save(any());

    }





    /*
        Test 3 :
        Recherche par téléphone
    */
    @Test
    void shouldFindUserByTelephone(){


        User user = new User(
                "Ali",
                "Mané",
                "ali@gmail.com",
                "772222222",
                "password",
                Role.USER
        );



        when(userRepository.findByTelephone("772222222"))
                .thenReturn(Optional.of(user));



        User foundUser =
                userService.findByTelephone("772222222");



        assertThat(foundUser)
                .isNotNull();


        assertThat(foundUser.getNom())
                .isEqualTo("Ali");

    }


    @Test
    void shouldLoginWithEmail() {

        User user = new User(
                "Aliou",
                "Mané",
                "aliou@gmail.com",
                "771234567",
                "password-hashe",
                Role.USER
        );

        when(userRepository.findByEmail("aliou@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password",
                "password-hashe"
        )).thenReturn(true);

        User loggedUser =
                userService.login(
                        "aliou@gmail.com",
                        "password"
                );

        assertThat(loggedUser)
                .isNotNull();

        assertThat(loggedUser.getEmail())
                .isEqualTo("aliou@gmail.com");

        verify(userRepository)
                .findByEmail("aliou@gmail.com");

        verify(passwordEncoder)
                .matches("password", "password-hashe");
    }


    @Test
    void shouldLoginWithTelephone() {

        User user = new User(
                "Aliou",
                "Mané",
                "aliou@gmail.com",
                "771234567",
                "password-hashe",
                Role.USER
        );

        when(userRepository.findByTelephone("771234567"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password",
                "password-hashe"
        )).thenReturn(true);

        User loggedUser =
                userService.login(
                        "771234567",
                        "password"
                );

        assertThat(loggedUser)
                .isNotNull();

        assertThat(loggedUser.getTelephone())
                .isEqualTo("771234567");

        verify(userRepository)
                .findByTelephone("771234567");

        verify(passwordEncoder)
                .matches("password", "password-hashe");
    }


    @Test
    void shouldNotLoginWithWrongPassword() {

        User user = new User(
                "Aliou",
                "Mané",
                "aliou@gmail.com",
                "771234567",
                "password-hashe",
                Role.USER
        );

        when(userRepository.findByEmail("aliou@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong-password",
                "password-hashe"
        )).thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> userService.login(
                        "aliou@gmail.com",
                        "wrong-password"
                )
        );
    }
}