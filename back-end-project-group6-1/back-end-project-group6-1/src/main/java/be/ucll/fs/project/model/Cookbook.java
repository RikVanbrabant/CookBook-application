package be.ucll.fs.project.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Table(name="cookbooks")
@Entity
public class Cookbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private int numOfRecipes;

    @OneToMany(mappedBy = "cookbook", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Recipe> recipes = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    protected Cookbook() {}

    public Cookbook(String title, int numOfRecipes, User user) {
        this.title = title;
        this.numOfRecipes = numOfRecipes;
        this.user = user;
    }

    public Cookbook(String title, int numOfRecipes) {
        this(title, numOfRecipes, null);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setCookbook(this);
        this.setNumOfRecipes(recipes.size());
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
    public String getOwner() {
        return user != null ? user.getUsername() : null;
    }
}