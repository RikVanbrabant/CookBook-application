package be.ucll.se.courses.backend.unit.service;

import be.ucll.fs.project.controller.dto.UserInput;
import be.ucll.fs.project.exception.CoursesException;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.UserRepository;
import be.ucll.fs.project.service.JwtService;
import be.ucll.fs.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user1 = new User("user1", "First", "Last", "email@ucll.be", "pw", Role.USER);
        when(userRepository.findAll()).thenReturn(List.of(user1));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void signup_shouldSaveUser_whenUsernameIsUnique() {
        UserInput input = new UserInput("newuser", "New", "User", "new@test.com", "password123");

        when(userRepository.existsByUsername(input.username())).thenReturn(false);
        when(passwordEncoder.encode(input.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.signup(Role.USER, input);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_shouldThrowException_whenUsernameAlreadyExists() {
        UserInput input = new UserInput("existing", "Test", "User", "test@test.com", "pw");

        when(userRepository.existsByUsername(input.username())).thenReturn(true);

        assertThrows(CoursesException.class, () -> userService.signup(Role.USER, input));
        verify(userRepository, never()).save(any(User.class));
    }
}