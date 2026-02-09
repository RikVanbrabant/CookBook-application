package be.ucll.fs.project.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.service.CookbookService;
import be.ucll.fs.project.service.RecipeService;

@RestController
@RequestMapping("/cookbooks")
@CrossOrigin(origins = "http://localhost:8080")
public class CookbookController {
    private CookbookService cookbookService;
    private RecipeService recipeService;

    public CookbookController(CookbookService cookbookService, RecipeService recipeService) {
        this.cookbookService = cookbookService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<Cookbook> getAllCookbooks() {
        return cookbookService.getAllCookbooks();
    }

    @GetMapping("/{id}")
    public Cookbook getCookbookById(@PathVariable(value = "id") Long id) {
        return cookbookService.getCookbookById(id);
    }
    
    @GetMapping("/{cookbook_id}/recipes")
    public List<Recipe> getRecipesByCookbookId(@PathVariable(value = "cookbook_id") Long cookbook_id) {
        return recipeService.getRecipesByCookbookId(cookbook_id);
    }
    @PostMapping("/{cookbook_id}/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe addRecipeToCookbook(@PathVariable(value = "cookbook_id") Long cookbook_id, @Valid @RequestBody Recipe recipe) {
        return recipeService.addRecipeToCookbook(cookbook_id, recipe);
    }
}