package com.example.backend.service.mail;

public interface MailService {
    void sendPasswordResetEmail(String email, String token);
    void sendEmailVerification(String email, String token);
}
