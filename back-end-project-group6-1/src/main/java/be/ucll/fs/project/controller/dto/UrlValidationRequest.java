package be.ucll.fs.project.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UrlValidationRequest(
        @NotBlank(message = "URL is required.")
        String url
) {}