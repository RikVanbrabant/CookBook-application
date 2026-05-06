package be.ucll.fs.project.service;

import java.util.List;

import be.ucll.fs.project.controller.dto.RecipeInput;
import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.CookbookRepository;
import be.ucll.fs.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.repository.RecipeRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CookbookRepository cookbookRepository;
    private final UserRepository userRepository;
    private final InputSecurityService inputSecurityService;

    public RecipeService(RecipeRepository recipeRepository, CookbookRepository cookbookRepository, UserRepository userRepository, InputSecurityService inputSecurityService) {
        this.recipeRepository = recipeRepository;
        this.cookbookRepository = cookbookRepository;
        this.userRepository = userRepository;
        this.inputSecurityService = inputSecurityService;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> searchRecipesByTitle(String title) {
        String cleanedTitle = inputSecurityService.cleanPlainText(title, "title");
        return recipeRepository.findByTitleContainingIgnoreCase(cleanedTitle);
    }

    public List<Recipe> getRecipesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return recipeRepository.findByCookbookUserId(user.getId());
    }


    public List<Recipe> getRecipesByCookbookId(Long cookbook_id) {
        return recipeRepository.findByCookbookId(cookbook_id);
    }

    public Recipe addRecipeToCookbook(Long cookbookId, RecipeInput recipeInput, String username) {
        Cookbook cookbook = cookbookRepository.findById(cookbookId)
                .orElseThrow(() -> new RuntimeException("Cookbook not found with id " + cookbookId));

        if (cookbook.getUser() == null || !cookbook.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have permission to add recipes to this cookbook.");
        }

        String safeTitle = inputSecurityService.cleanPlainText(recipeInput.title(), "title");
        Recipe recipe = new Recipe(safeTitle, recipeInput.prepTime(), recipeInput.cookTime());

        cookbook.addRecipe(recipe);
        Recipe saved = recipeRepository.save(recipe);
        cookbookRepository.save(cookbook);
        return saved;
    }

    public Recipe updateRecipe(Long id, RecipeInput recipeInput, String username) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Cookbook cookbook = recipe.getCookbook();

        if (cookbook == null || cookbook.getUser() == null || !cookbook.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have permission to edit this recipe.");
        }

        recipe.setTitle(inputSecurityService.cleanPlainText(recipeInput.title(), "title"));
        recipe.setPrepTime(recipeInput.prepTime());
        recipe.setCookTime(recipeInput.cookTime());

        return recipeRepository.save(recipe);
    }

    public void deleteRecipeFromCookbook(Long recipe_id, String username) {
        Recipe recipe = recipeRepository.findById(recipe_id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cookbook cookbook = recipe.getCookbook();

        boolean isOwner = cookbook != null &&
                cookbook.getUser() != null &&
                cookbook.getUser().getUsername().equals(username);

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You do not have permission to delete this recipe.");
        }

        if (cookbook != null) {
            cookbook.getRecipes().remove(recipe);
            cookbook.setNumOfRecipes(cookbook.getRecipes().size());
            cookbookRepository.save(cookbook);
        }

        recipeRepository.delete(recipe);
    }
}