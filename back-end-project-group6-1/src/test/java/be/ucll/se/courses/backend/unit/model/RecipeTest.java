package be.ucll.se.courses.backend.unit.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.fs.project.model.Recipe;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createRecipe_shouldBeValid() {
        Recipe recipe = new Recipe("Healthy Salad", 15, 5);

        var violations = validator.validate(recipe);

        assertTrue(violations.isEmpty());
        assertEquals("Healthy Salad", recipe.getTitle());
    }

    @Test
    void createRecipe_withInvalidPrepTime_shouldFailValidation() {
        Recipe recipe = new Recipe("Quick Snack", -1, 5);

        var violations = validator.validate(recipe);

        assertFalse(violations.isEmpty(), "Validatie zou moeten falen bij prepTime = 0");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("non-negative") || v.getMessage().contains("groter dan of gelijk aan 0")));    }
}