package be.ucll.fs.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be.ucll.fs.project.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCookbookId(Long cookbookId);
    List<Recipe> findByCookbookUserId(Long userId);
    List<Recipe> findByTitleContainingIgnoreCase(String title);
}
