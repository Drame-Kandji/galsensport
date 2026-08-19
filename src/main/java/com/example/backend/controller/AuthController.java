package com.example.backend.controller;

import com.example.backend.dto.auth.*;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.entity.AccountTokenPurpose;
import com.example.backend.service.auth.AuthenticationTokenService;
import com.example.backend.service.mail.MailService;
import com.example.backend.service.user.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationTokenService authenticationTokenService;
    private final MailService mailService;

    public AuthController(
            UserService userService,
            UserMapper userMapper,
            AuthenticationTokenService authenticationTokenService,
            MailService mailService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationTokenService = authenticationTokenService;
        this.mailService = mailService;
    }

    // =========================================================
    // INSCRIPTION USER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {

        User user =
                userService.createUser(request);

        sendEmailVerification(user);

        AuthResponse response =
                userMapper.toAuthResponse(
                        user,
                        authenticationTokenService.createSession(user)
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // INSCRIPTION ENTREPRISE
    // =========================================================

    @PostMapping("/register/company")
    public ResponseEntity<AuthResponse> registerCompany(
            @Valid @RequestBody RegisterCompanyRequest request
    ) {

        User user =
                userService.createCompany(request);

        sendEmailVerification(user);

        AuthResponse response =
                userMapper.toAuthResponse(
                        user,
                        authenticationTokenService.createSession(user)
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // CONNEXION
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        User user =
                userService.login(
                        request.getLogin(),
                        request.getPassword()
                );

        AuthResponse response =
                userMapper.toAuthResponse(
                        user,
                        authenticationTokenService.createSession(user)
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        User user = authenticationTokenService.rotateRefreshToken(request.refreshToken());
        return ResponseEntity.ok(userMapper.toAuthResponse(
                user,
                authenticationTokenService.createSession(user)
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        authenticationTokenService.revokeRefreshToken(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        User user = userService.findByEmail(request.email());
        if (user != null && user.isEnabled()) {
            String token = authenticationTokenService.createAccountToken(
                    user, AccountTokenPurpose.PASSWORD_RESET
            );
            mailService.sendPasswordResetEmail(user.getEmail(), token);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        User user = authenticationTokenService.consumeAccountToken(
                request.token(), AccountTokenPurpose.PASSWORD_RESET
        );
        userService.resetPassword(user, request.newPassword());
        authenticationTokenService.revokeAllUserSessions(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            org.springframework.security.core.Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user, request.currentPassword(), request.newPassword());
        authenticationTokenService.revokeAllUserSessions(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        User user = authenticationTokenService.consumeAccountToken(
                token, AccountTokenPurpose.EMAIL_VERIFICATION
        );
        user.setEmailVerified(true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/verification/resend")
    public ResponseEntity<Void> resendEmailVerification(
            org.springframework.security.core.Authentication authentication
    ) {
        sendEmailVerification((User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    private void sendEmailVerification(User user) {
        if (user.isEmailVerified()) {
            return;
        }
        String token = authenticationTokenService.createAccountToken(
                user, AccountTokenPurpose.EMAIL_VERIFICATION
        );
        mailService.sendEmailVerification(user.getEmail(), token);
    }
}
