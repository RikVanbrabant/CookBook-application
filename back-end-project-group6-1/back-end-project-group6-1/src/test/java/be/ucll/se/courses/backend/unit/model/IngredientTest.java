package be.ucll.se.courses.backend.unit.model;

import be.ucll.fs.project.model.Ingredient;
import be.ucll.fs.project.model.Recipe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void addRecipe_shouldLinkBothSides() {
        Ingredient tomato = new Ingredient("Tomato");
        Recipe soup = new Recipe("Soup", 10, 20);

        tomato.addRecipe(soup);

        assertTrue(tomato.getRecipes().contains(soup), "Ingredient moet referentie naar Recipe hebben");
        assertTrue(soup.getIngredients().contains(tomato), "Recipe moet referentie naar Ingredient hebben");
    }
}