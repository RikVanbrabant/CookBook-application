package be.ucll.se.courses.backend.unit.service;

import be.ucll.fs.project.Application;
import be.ucll.fs.project.controller.dto.RecipeInput;
import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.CookbookRepository;
import be.ucll.fs.project.repository.RecipeRepository;
import be.ucll.fs.project.service.RecipeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class RecipeServiceTest {

    @MockBean
    private CookbookRepository cookbookRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addRecipeToCookbook_shouldSaveRecipeAndCookbook() {
        Long cookbookId = 1L;
        String username = "admin";

        User user = new User(username, "Ad", "Min", "admin@test.com", "pass", Role.ADMIN);
        Cookbook cookbook = new Cookbook("Italian", 0, user);

        RecipeInput input = new RecipeInput("Pizza", 20, 15);

        when(cookbookRepository.findById(cookbookId)).thenReturn(Optional.of(cookbook));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(i -> i.getArguments()[0]);

        Recipe result = recipeService.addRecipeToCookbook(cookbookId, input, username);

        assertNotNull(result);
        assertEquals(1, cookbook.getNumOfRecipes());
        verify(cookbookRepository).save(cookbook);
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllRecipes_shouldReturnAllRecipes() {
        Recipe r1 = new Recipe("Lasagna", 30, 45);
        List<Recipe> expectedRecipes = List.of(r1);

        when(recipeRepository.findAll()).thenReturn(expectedRecipes);

        List<Recipe> result = recipeService.getAllRecipes();

        assertEquals(1, result.size());
        assertEquals("Lasagna", result.get(0).getTitle());
    }

    @Test
    @WithMockUser
    void getRecipesByCookbookId_shouldReturnCorrectRecipes() {
        Long cookbookId = 2L;
        List<Recipe> expectedRecipes = List.of(new Recipe("French Soup", 10, 20));

        when(recipeRepository.findByCookbookId(cookbookId)).thenReturn(expectedRecipes);

        List<Recipe> result = recipeService.getRecipesByCookbookId(cookbookId);

        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser
    void addRecipeToCookbook_shouldThrowExceptionIfCookbookNotFound() {
        Long nonExistentId = 99L;
        RecipeInput input = new RecipeInput("Fails", 10, 10);
        String username = "user";

        when(cookbookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            recipeService.addRecipeToCookbook(nonExistentId, input, username);
        });

        assertTrue(thrown.getMessage().contains("Cookbook not found"));
    }
}