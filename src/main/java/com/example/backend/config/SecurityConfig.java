package com.example.backend.config;

import com.example.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // =====================================================
                // CSRF
                // =====================================================

                .csrf(csrf -> csrf.disable())

                // =====================================================
                // AUTHENTIFICATION PAR FORMULAIRE / BASIC
                // =====================================================

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())

                // =====================================================
                // AUTORISATION
                // =====================================================

                .authorizeHttpRequests(auth -> auth

                        // -------------------------------------------------
                        // SWAGGER
                        // -------------------------------------------------

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // -------------------------------------------------
                        // AUTHENTIFICATION PUBLIQUE
                        // -------------------------------------------------

                        .requestMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/register/company",
                                "/api/v1/auth/login"
                        ).permitAll()

                        // -------------------------------------------------
                        // PROFIL USER
                        // -------------------------------------------------

                        .requestMatchers(
                                "/api/v1/users/me"
                        ).hasRole("USER")

                        // -------------------------------------------------
                        // PROFIL ENTREPRISE
                        // -------------------------------------------------

                        .requestMatchers(
                                "/api/v1/entreprises/me"
                        ).hasRole("ENTREPRISE")

                        // -------------------------------------------------
                        // PROFIL ADMIN
                        // -------------------------------------------------

                        .requestMatchers(
                                "/api/v1/admin/profile/**"
                        ).hasRole("ADMIN")

                        // -------------------------------------------------
                        // TOUT LE RESTE
                        // -------------------------------------------------

                        .anyRequest().authenticated()
                )

                // =====================================================
                // JWT FILTER
                // =====================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}