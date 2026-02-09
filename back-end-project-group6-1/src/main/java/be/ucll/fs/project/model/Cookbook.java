package be.ucll.fs.project.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Table(name="cookbooks")
@Entity
public class Cookbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int numOfRecipes;

    @OneToMany(mappedBy = "cookbook", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Recipe> recipes = new ArrayList<>();

    protected Cookbook() {}

    public Cookbook(String title, int numOfRecipes) {
        setTitle(title);
        setNumOfRecipes(numOfRecipes);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumOfRecipes() {
        return numOfRecipes;
    }

    public void setNumOfRecipes(int numOfRecipes) {
        this.numOfRecipes = numOfRecipes;
    }
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setCookbook(this);
        this.setNumOfRecipes(recipes.size());
    }
    public List<Recipe> getRecipes() {
        return recipes;
    }
}
