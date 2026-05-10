package be.ucll.fs.project.service;

import be.ucll.fs.project.exception.CoursesException;
import be.ucll.fs.project.model.PasswordResetToken;
import be.ucll.fs.project.repository.PasswordResetTokenRepository;
import be.ucll.fs.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                EmailService emailService,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    public void initiatePasswordReset(String email) {

        var userOptional = userRepository.findByEmail(email);

        String token = UUID.randomUUID().toString();
        passwordEncoder.encode(token);

        if (userOptional.isPresent()) {

            var user = userOptional.get();

            tokenRepository.deleteAllByUser(user);

            tokenRepository.save(
                    new PasswordResetToken(
                            token,
                            user,
                            LocalDateTime.now().plusMinutes(15)
                    )
            );

            emailService.sendPasswordResetEmail(user.getEmail(), token);

        }
    }
    public void resetPassword(String token, String newPassword) {
        var resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new CoursesException("Invalid or expired token"));

        if (resetToken.isExpiredOrUsed()) {
            throw new CoursesException("Invalid or expired token");
        }

        var user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
