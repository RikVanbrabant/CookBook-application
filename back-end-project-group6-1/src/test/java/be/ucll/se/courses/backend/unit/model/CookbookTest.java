package be.ucll.se.courses.backend.unit.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Recipe;

import static org.junit.jupiter.api.Assertions.*;

class CookbookTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void addRecipe_shouldAddRecipeAndIncrementCount() {
        Cookbook cookbook = new Cookbook("My Cookbook", 0);
        Recipe recipe = new Recipe("Pasta", 10, 10);

        cookbook.addRecipe(recipe);

        assertEquals(1, cookbook.getRecipes().size());
        assertEquals(1, cookbook.getNumOfRecipes());
        assertEquals(cookbook, recipe.getCookbook());
    }

    @Test
    void createCookbook_withEmptyTitle_shouldFailValidation() {
        Cookbook cookbook = new Cookbook("", 0); // Ongeldige titel

        var violations = validator.validate(cookbook);

        assertFalse(violations.isEmpty(), "Er zouden validatiefouten moeten zijn");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("niet leeg zijn") || v.getPropertyPath().toString().equals("title")));
    }
}