package be.ucll.fs.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Table(name="recipes")
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required.")
    private String title;

    @Min(value = 0, message = "Prep time must be a non-negative number.")
    private int prepTime;

    @Min(value = 0, message = "Cook time must be a non-negative number.")
    private int cookTime;

    @ManyToOne
    @JoinColumn(name = "cookbook_id")
    @JsonIgnore
    private Cookbook cookbook;

    protected Recipe() {}

    public Recipe(String title, int prepTime, int cookTime) {
        setTitle(title);
        setPrepTime(prepTime);
        setCookTime(cookTime);
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

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public Cookbook getCookbook() {
        return cookbook;
    }

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }
}
