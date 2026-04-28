package be.ucll.fs.project.repository;

import be.ucll.fs.project.model.Profile;
import be.ucll.fs.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}