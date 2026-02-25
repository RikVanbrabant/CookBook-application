package be.ucll.se.courses.backend.integration;

import be.ucll.fs.project.Application; // Zorg dat je deze import hebt!
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration; // Import nodig

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = Application.class) // <--- DE OPLOSSING
class UserIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindUser_shouldPersistToDatabase() {
        User user = new User("integUser", "Integration", "Test", "integ@ucll.be", "secret", Role.USER);

        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        User foundUser = userRepository.findByUsername("integUser").orElse(null);

        assertNotNull(foundUser);
        assertEquals("integ@ucll.be", foundUser.getEmail());
    }
}