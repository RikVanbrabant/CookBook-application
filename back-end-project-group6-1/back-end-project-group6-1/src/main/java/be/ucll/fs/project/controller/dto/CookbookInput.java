package be.ucll.fs.project.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CookbookInput(
        @NotBlank(message = "Title is required.")
        String title,
        @NotNull
        int numOfRecipes
) {}
