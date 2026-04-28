package be.ucll.fs.project.controller;

import be.ucll.fs.project.controller.dto.AuthenticationRequest;
import be.ucll.fs.project.controller.dto.AuthenticationResponse;
import be.ucll.fs.project.controller.dto.UserInput;
import be.ucll.fs.project.exception.NotFoundException;
import be.ucll.fs.project.model.Profile;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.ProfileRepository;
import be.ucll.fs.project.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.security.core.Authentication;

import org.springdoc.core.service.GenericResponseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final GenericResponseService responseBuilder;
    private final ProfileRepository profileRepository;

    public UserController(UserService userService, GenericResponseService responseBuilder, ProfileRepository profileRepository) {
        this.userService = userService;
        this.responseBuilder = responseBuilder;
        this.profileRepository = profileRepository;
    }
    @PostMapping("/signup")
    public User signup(@Valid @RequestBody UserInput userInput){
        return userService.signup(Role.GUEST, userInput);
    }
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response){
        var auth = userService.authenticate(authenticationRequest.username(), authenticationRequest.password());

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
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("authToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite(SameSiteCookies.NONE.toString())
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
}
