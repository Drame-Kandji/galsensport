package com.example.backend.service;

import com.example.backend.dto.auth.RegisterUserRequest;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.AdminProfileRepository;
import com.example.backend.repository.EntrepriseRepository;
import com.example.backend.repository.UserProfileRepository;
import com.example.backend.repository.UserRepository;

import com.example.backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private AdminProfileRepository adminProfileRepository;

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    /*
     * ============================================================
     * TEST 1 : Création d'un utilisateur
     * ============================================================
     */
    @Test
    void shouldCreateUser() {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setNom("Sadio");
        request.setPrenom("Mané");
        request.setEmail("sadio@gmail.com");
        request.setTelephone("771234567");
        request.setPassword("password");


        when(userRepository.existsByEmail("sadio@gmail.com"))
                .thenReturn(false);

        when(userRepository.existsByTelephone("771234567"))
                .thenReturn(false);

        when(passwordEncoder.encode("password"))
                .thenReturn("password-hashe");


        User savedUser = new User(
                "sadio@gmail.com",
                "771234567",
                "password-hashe",
                Role.USER
        );


        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);


        User result =
                userService.createUser(request);


        assertThat(result)
                .isNotNull();

        assertThat(result.getEmail())
                .isEqualTo("sadio@gmail.com");

        assertThat(result.getTelephone())
                .isEqualTo("771234567");

        assertThat(result.getRole())
                .isEqualTo(Role.USER);

        verify(passwordEncoder)
                .encode("password");

        verify(userRepository)
                .save(any(User.class));

        verify(userProfileRepository)
                .save(any());
    }


    /*
     * ============================================================
     * TEST 2 : Email déjà utilisé
     * ============================================================
     */
    @Test
    void shouldNotCreateUserWithExistingEmail() {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setNom("Mamadou");
        request.setPrenom("Mané");
        request.setEmail("mamadou@gmail.com");
        request.setTelephone("771111111");
        request.setPassword("password");


        when(userRepository.existsByEmail("mamadou@gmail.com"))
                .thenReturn(true);


        assertThrows(
                RuntimeException.class,
                () -> userService.createUser(request)
        );


        verify(userRepository, never())
                .save(any());

        verify(userProfileRepository, never())
                .save(any());
    }


    /*
     * ============================================================
     * TEST 3 : Recherche par téléphone
     * ============================================================
     */
    @Test
    void shouldFindUserByTelephone() {

        User user = new User(
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

        assertThat(foundUser.getEmail())
                .isEqualTo("ali@gmail.com");

        assertThat(foundUser.getTelephone())
                .isEqualTo("772222222");

        assertThat(foundUser.getRole())
                .isEqualTo(Role.USER);
    }


    /*
     * ============================================================
     * TEST 4 : Connexion avec email
     * ============================================================
     */
    @Test
    void shouldLoginWithEmail() {

        User user = new User(
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
                .matches(
                        "password",
                        "password-hashe"
                );
    }


    /*
     * ============================================================
     * TEST 5 : Connexion avec téléphone
     * ============================================================
     */
    @Test
    void shouldLoginWithTelephone() {

        User user = new User(
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
                .matches(
                        "password",
                        "password-hashe"
                );
    }


    /*
     * ============================================================
     * TEST 6 : Mauvais mot de passe
     * ============================================================
     */
    @Test
    void shouldNotLoginWithWrongPassword() {

        User user = new User(
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