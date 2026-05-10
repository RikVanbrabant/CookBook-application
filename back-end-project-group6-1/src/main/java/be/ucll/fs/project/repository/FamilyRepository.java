package be.ucll.fs.project.repository;

import be.ucll.fs.project.model.Family;
import be.ucll.fs.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    List<Family> findByMembersContaining(User user);

    List<Family> findByHeadOfFamily(User headOfFamily);
}
