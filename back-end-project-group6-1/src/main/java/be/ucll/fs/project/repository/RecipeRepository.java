package be.ucll.fs.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be.ucll.fs.project.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value = "SELECT * FROM recipes", nativeQuery = true)
    List<Recipe> getAllRecipes();

    @Query(value = "SELECT * from recipes WHERE cookbook_id = :cookbook_id", nativeQuery = true)
    List<Recipe> getRecipesByCookbookId(@Param("cookbook_id") Long cookbook_id);
}
