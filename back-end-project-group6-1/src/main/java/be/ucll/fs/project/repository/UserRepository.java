package be.ucll.fs.project.repository;

import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    void deleteAll();

    Optional<User> findById(Long id);

    List<User> findAll();

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
