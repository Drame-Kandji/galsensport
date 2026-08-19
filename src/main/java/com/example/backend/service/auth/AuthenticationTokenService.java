package com.example.backend.service.auth;

import com.example.backend.entity.*;
import com.example.backend.exception.InvalidCredentialsException;
import com.example.backend.repository.AccountTokenRepository;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.service.jwt.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
public class AuthenticationTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountTokenRepository accountTokenRepository;
    private final JwtService jwtService;
    private final long refreshExpiration;
    private final long accountTokenExpiration;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthenticationTokenService(
            RefreshTokenRepository refreshTokenRepository,
            AccountTokenRepository accountTokenRepository,
            JwtService jwtService,
            @Value("${jwt.refresh-expiration:2592000000}") long refreshExpiration,
            @Value("${security.account-token-expiration:3600000}") long accountTokenExpiration
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountTokenRepository = accountTokenRepository;
        this.jwtService = jwtService;
        this.refreshExpiration = refreshExpiration;
        this.accountTokenExpiration = accountTokenExpiration;
    }

    public SessionTokens createSession(User user) {

        String rawRefreshToken = newRandomToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID());
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash(rawRefreshToken));
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        refreshTokenRepository.save(refreshToken);

        return new SessionTokens(
                jwtService.generateToken(user),
                rawRefreshToken,
                jwtService.getExpirationInSeconds()
        );
    }

    public User rotateRefreshToken(String rawRefreshToken) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> new InvalidCredentialsException("Session invalide ou expirée"));

        if (refreshToken.getRevokedAt() != null
                || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())
                || !refreshToken.getUser().isEnabled()) {
            throw new InvalidCredentialsException("Session invalide ou expirée");
        }

        refreshToken.setRevokedAt(LocalDateTime.now());
        return refreshToken.getUser();
    }

    public void revokeRefreshToken(String rawRefreshToken) {
        refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .ifPresent(token -> token.setRevokedAt(LocalDateTime.now()));
    }

    public void revokeAllUserSessions(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public String createAccountToken(User user, AccountTokenPurpose purpose) {

        accountTokenRepository.deleteByUserAndPurpose(user, purpose);
        String rawToken = newRandomToken();
        AccountToken token = new AccountToken();
        token.setId(UUID.randomUUID());
        token.setUser(user);
        token.setTokenHash(hash(rawToken));
        token.setPurpose(purpose);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusSeconds(accountTokenExpiration / 1000));
        accountTokenRepository.save(token);
        return rawToken;
    }

    public User consumeAccountToken(String rawToken, AccountTokenPurpose purpose) {

        AccountToken token = accountTokenRepository
                .findByTokenHashAndPurpose(hash(rawToken), purpose)
                .orElseThrow(() -> new InvalidCredentialsException("Lien invalide ou expiré"));

        if (token.getUsedAt() != null || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Lien invalide ou expiré");
        }

        token.setUsedAt(LocalDateTime.now());
        return token.getUser();
    }

    private String newRandomToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de sécuriser le jeton", exception);
        }
    }

    public record SessionTokens(String accessToken, String refreshToken, long expiresIn) {
    }
}
