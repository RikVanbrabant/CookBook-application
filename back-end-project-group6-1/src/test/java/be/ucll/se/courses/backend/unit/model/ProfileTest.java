package be.ucll.se.courses.backend.unit.model;

import be.ucll.fs.project.model.Profile;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createProfile_shouldWork_whenDataIsValid() {
        User user = new User("jan", "Jan", "J", "j@j.com", "pw", Role.USER);
        Profile profile = new Profile(user, "Jan de Kok", "Ik hou van koken");

        var violations = validator.validate(profile);

        assertTrue(violations.isEmpty());
        assertEquals("Jan de Kok", profile.getFullName());
    }

    @Test
    void createProfile_shouldFail_whenFullNameIsBlank() {
        User user = new User("jan", "Jan", "J", "j@j.com", "pw", Role.USER);
        Profile profile = new Profile(user, "", "Ik hou van koken");

        var violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Full name is required")));
    }
}