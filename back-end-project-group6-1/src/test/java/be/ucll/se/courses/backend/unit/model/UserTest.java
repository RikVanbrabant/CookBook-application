package be.ucll.se.courses.backend.unit.model;

import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createUser_shouldWork_whenDataIsValid() {
        User user = new User("jan123", "Jan", "Janssens", "jan@test.be", "wachtwoord", Role.USER);

        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Er zouden geen validatiefouten moeten zijn voor een geldige gebruiker");
        assertEquals("Jan Janssens", user.getFullName());
    }

    @Test
    void createUser_shouldFail_whenEmailIsInvalid() {
        User user = new User("jan123", "Jan", "Janssens", "geengeldigemail", "wachtwoord", Role.USER);

        var violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void createUser_shouldFail_whenFieldsAreBlank() {
        User user = new User("", "", "", "", "", Role.USER);

        var violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("required")));
    }
}