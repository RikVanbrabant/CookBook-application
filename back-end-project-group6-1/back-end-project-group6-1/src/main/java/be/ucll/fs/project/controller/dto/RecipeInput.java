package be.ucll.fs.project.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecipeInput(
        @NotBlank(message = "Title is required.")
        String title,

        @Min(value = 0, message = "Prep time must be a non-negative number.")
        int prepTime,

        @Min(value = 0, message = "Cook time must be a non-negative number.")
        int cookTime

) {}
