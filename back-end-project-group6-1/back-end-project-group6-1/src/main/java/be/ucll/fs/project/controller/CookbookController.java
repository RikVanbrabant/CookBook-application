package be.ucll.fs.project.controller;

import java.security.Principal;
import java.util.List;

import be.ucll.fs.project.controller.dto.CookbookInput;
import be.ucll.fs.project.controller.dto.RecipeInput;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.service.CookbookService;
import be.ucll.fs.project.service.RecipeService;

@RestController
@RequestMapping("/cookbooks")
public class CookbookController {
    private final CookbookService cookbookService;
    private final RecipeService recipeService;

    public CookbookController(CookbookService cookbookService, RecipeService recipeService) {
        this.cookbookService = cookbookService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<Cookbook> getAllCookbooks(
            @RequestParam(required = false, defaultValue = "false") boolean mine,
            Principal principal) {
        if (mine && principal != null) {
            return cookbookService.getCookbooksByUsername(principal.getName());
        }
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Recipe addRecipeToCookbook(
            @PathVariable(value = "cookbook_id") Long cookbook_id,
            @Valid @RequestBody RecipeInput recipeInput,
            Principal principal) {
        return recipeService.addRecipeToCookbook(cookbook_id, recipeInput, principal.getName());
    }
    @PostMapping
    public Cookbook createCookbook(@Valid @RequestBody CookbookInput cookbookInput, Authentication authentication) {
        return cookbookService.createCookbook(cookbookInput, authentication.getName());
    }
}