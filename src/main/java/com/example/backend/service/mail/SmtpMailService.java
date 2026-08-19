package com.example.backend.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailService implements MailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpMailService.class);

    private final JavaMailSender mailSender;
    private final String frontendUrl;
    private final String from;

    public SmtpMailService(
            JavaMailSender mailSender,
            @Value("${app.frontend-url:http://localhost:4200}") String frontendUrl,
            @Value("${spring.mail.from:no-reply@galsensport.local}") String from
    ) {
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl.replaceAll("/$", "");
        this.from = from;
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        send(email, "Réinitialisez votre mot de passe", "Utilisez ce lien : "
                + frontendUrl + "/auth/reset-password?token=" + token);
    }

    @Override
    public void sendEmailVerification(String email, String token) {
        send(email, "Vérifiez votre adresse e-mail", "Confirmez votre adresse : "
                + frontendUrl + "/auth/verify-email?token=" + token);
    }

    private void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("E-mail transactionnel envoyé à {}", to);
    }
}
