package be.ucll.fs.project.service;

import java.util.List;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.repository.CookbookRepository;
import org.springframework.stereotype.Service;

import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.repository.RecipeRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private CookbookRepository cookbookRepository;

    public RecipeService(RecipeRepository recipeRepository, CookbookRepository cookbookRepository) {
        this.recipeRepository = recipeRepository;
        this.cookbookRepository = cookbookRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.getAllRecipes();
    }

    public List<Recipe> getRecipesByCookbookId(Long cookbook_id) {
        return recipeRepository.getRecipesByCookbookId(cookbook_id);
    }
    @Transactional
    public Recipe addRecipeToCookbook(Long cookbookId, Recipe recipe) {
        Cookbook cookbook = cookbookRepository.findById(cookbookId)
                .orElseThrow(() -> new RuntimeException("Cookbook not found with id " + cookbookId));
        cookbook.addRecipe(recipe);
        cookbookRepository.save(cookbook);
        return recipe;
    }
}
