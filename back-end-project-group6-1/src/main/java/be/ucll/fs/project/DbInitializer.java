package be.ucll.fs.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.repository.CookbookRepository;
import be.ucll.fs.project.repository.RecipeRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {
    private CookbookRepository cookbookRepository;
    private RecipeRepository recipeRepository;

    @Autowired
    public DbInitializer(CookbookRepository cookbookRepository, RecipeRepository recipeRepository) {
        this.cookbookRepository = cookbookRepository;
        this.recipeRepository = recipeRepository;
    }

    @PostConstruct
    public void initialize() {
        Cookbook cookbook1 = new Cookbook("Italian Favourites", 4);
        Cookbook cookbook2 = new Cookbook("Indian Favourites", 0);

        Recipe recipe1 = new Recipe("Spaghetti Carbonara", 15, 20);
        Recipe recipe2 = new Recipe("Spaghetti Bolo", 15, 20);
        Recipe recipe3 = new Recipe("Pasta Pesto", 15, 20);
        Recipe recipe4 = new Recipe("Tagliathelli ", 15, 20);

        cookbookRepository.save(cookbook1);
        cookbookRepository.save(cookbook2);

        recipe1.setCookbook(cookbook1);
        recipe2.setCookbook(cookbook1);
        recipe3.setCookbook(cookbook1);
        recipe4.setCookbook(cookbook1);

        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);
        recipeRepository.save(recipe3);
        recipeRepository.save(recipe4);
    }
}
