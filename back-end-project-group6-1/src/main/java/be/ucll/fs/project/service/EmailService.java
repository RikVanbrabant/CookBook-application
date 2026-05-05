package be.ucll.fs.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:8080}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@cookbook.app");
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText(
                "Hello,\n\n" +
                        "Click the link below to reset your password. It expires in 15 minutes.\n\n" +
                        resetLink + "\n\n" +
                        "If you did not request this, ignore this email.\n\n" +
                        "— Cookbook App"
        );
        mailSender.send(message);
    }
}
