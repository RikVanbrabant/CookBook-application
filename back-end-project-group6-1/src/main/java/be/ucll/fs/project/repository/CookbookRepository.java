package be.ucll.fs.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be.ucll.fs.project.model.Cookbook;

@Repository
public interface CookbookRepository extends JpaRepository<Cookbook, Long> {
    List<Cookbook> findByUserId(Long userId);
    List<Cookbook> findByFamilyId(Long familyId);

}
