package be.ucll.fs.project.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserInput(
        @NotBlank
        String username,
        @NotBlank
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$", 
            message = "Wachtwoord moet minstens 8 karakters lang zijn, 1 hoofdletter bevatten en 1 cijfer of speciaal teken."
        )
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        String email) {
}
