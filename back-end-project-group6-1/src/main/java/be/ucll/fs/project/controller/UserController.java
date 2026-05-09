package be.ucll.fs.project.controller;

import be.ucll.fs.project.controller.dto.AuthenticationRequest;
import be.ucll.fs.project.controller.dto.AuthenticationResponse;
import be.ucll.fs.project.controller.dto.ForgotPasswordRequest;
import be.ucll.fs.project.controller.dto.ResetPasswordRequest;
import be.ucll.fs.project.controller.dto.UserInput;
import be.ucll.fs.project.exception.NotFoundException;
import be.ucll.fs.project.model.Profile;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.ProfileRepository;
import be.ucll.fs.project.service.UserService;
import be.ucll.fs.project.service.PasswordResetService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import org.springdoc.core.service.GenericResponseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY_AUDIT");

    private final UserService userService;
    private final GenericResponseService responseBuilder;
    private final ProfileRepository profileRepository;
    private final PasswordResetService passwordResetService;

    public UserController(UserService userService, GenericResponseService responseBuilder, ProfileRepository profileRepository, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.responseBuilder = responseBuilder;
        this.profileRepository = profileRepository;
        this.passwordResetService = passwordResetService;
    }

    private String mask(String input) {
        if (input == null || input.isBlank()) return "unknown";
        if (input.contains("@")) {
            String[] parts = input.split("@", 2);
            if (parts[0].length() <= 2) return "**@" + parts[1];
            return parts[0].substring(0, 2) + "***" + parts[0].substring(parts[0].length() - 1) + "@" + parts[1];
        }
        if (input.length() <= 2) return "**";
        return input.substring(0, 2) + "***" + input.substring(input.length() - 1);
    }

    @PostMapping("/signup")
    public User signup(@Valid @RequestBody UserInput userInput){
        User user = userService.signup(Role.GUEST, userInput);
        securityLogger.info("event=signup_success, user={}, message=Signup successful", mask(userInput.email()));
        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response){
        try {
            var auth = userService.authenticate(authenticationRequest.username(), authenticationRequest.password());

            securityLogger.info("event=authentication_success, user={}, message=Login successful", mask(authenticationRequest.username()));

            ResponseCookie cookie = ResponseCookie.from("authToken", auth.token())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(3600)
                    .sameSite(SameSiteCookies.LAX.toString())
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            auth = new AuthenticationResponse(auth.message(), null, auth.username(), auth.fullname(), auth.role());
            return ResponseEntity.ok(auth);
        } catch (Exception e) {
            securityLogger.warn("event=authentication_failure, user={}, message=Login failed", mask(authenticationRequest.username()));
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        securityLogger.info("event=logout_success, message=Logout successful");
        ResponseCookie cookie = ResponseCookie.from("authToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite(SameSiteCookies.LAX.toString())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public Profile getProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));

        return profileRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.initiatePasswordReset(request.email());
        return ResponseEntity.ok(Map.of("message", "If this email is registered, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }
}