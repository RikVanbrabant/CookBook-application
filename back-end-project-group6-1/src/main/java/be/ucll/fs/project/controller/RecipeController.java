package be.ucll.fs.project.controller;

import java.security.Principal;
import java.util.List;

import be.ucll.fs.project.controller.dto.RecipeInput;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.service.RecipeService;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<Recipe> getAllRecipes(
            @RequestParam(required = false, defaultValue = "false") boolean mine,
            Principal principal) {
        if (mine && principal != null) {
            return recipeService.getRecipesByUsername(principal.getName());
        }
        return recipeService.getAllRecipes();
    }
    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam String title) {
        return recipeService.searchRecipesByTitle(title);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Recipe updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeInput recipeInput,
            Principal principal) {
        return recipeService.updateRecipe(id, recipeInput, principal.getName());
    }

    @DeleteMapping("/{recipe_id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public void deleteRecipeFromCookbook(@PathVariable Long recipe_id, Principal principal) {
        recipeService.deleteRecipeFromCookbook(recipe_id, principal.getName());
    }
}