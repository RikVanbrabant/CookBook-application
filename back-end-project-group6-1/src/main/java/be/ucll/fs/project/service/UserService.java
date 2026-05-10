package be.ucll.fs.project.service;

import be.ucll.fs.project.controller.dto.AuthenticationResponse;
import be.ucll.fs.project.controller.dto.UserInput;
import be.ucll.fs.project.controller.dto.UserPatchRequest;
import be.ucll.fs.project.exception.CoursesException;
import be.ucll.fs.project.exception.NotFoundException;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }
    public AuthenticationResponse authenticate(String username, String password) {
        final var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        final var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        final var user = ((UserDetailsImpl) authentication.getPrincipal()).user();
        final var token = jwtService.generateToken(user);
        return new AuthenticationResponse(
                "Authentication successful.",
                token,
                user.getUsername(),
                user.getFullName(),
                user.getRole()
        );
    }

    public User signup(Role role, UserInput userInput) {
        if (userRepository.existsByUsername(userInput.username())){
            throw new CoursesException("Username is already in use");
        }
        final var hashedPassword = passwordEncoder.encode(userInput.password());
        final var user = new User(
                userInput.username(),
                userInput.firstName(),
                userInput.lastName(),
                userInput.email(),
                hashedPassword,
                role
        );

        return userRepository.save(user);
    }
    public User patchUser(Long targetUserId, UserPatchRequest patch, String callerUsername) {
        User caller = userRepository.findByUsername(callerUsername)
                .orElseThrow(() -> new NotFoundException("Caller not found"));

        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean isAdmin = caller.getRole() == Role.ADMIN;
        boolean isSelf = caller.getId().equals(target.getId());

        if (!isAdmin && !isSelf) {
            throw new AccessDeniedException("You can only update your own account.");
        }

        if (patch.firstName() != null) {
            target.setFirstName(patch.firstName());
        }
        if (patch.lastName() != null) {
            target.setLastName(patch.lastName());
        }
        if (patch.email() != null) {
            target.setEmail(patch.email());
        }

        if (patch.role() != null) {
            if (!isAdmin) {
                throw new AccessDeniedException(
                        "Only admins can change a user's role.");
            }
            target.setRole(patch.role());
        }

        return userRepository.save(target);
    }
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }
}

