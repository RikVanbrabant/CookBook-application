package be.ucll.fs.project.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Token is required")
        String token,

        @NotBlank(message = "New password is required")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
                message = "Wachtwoord moet minstens 8 karakters lang zijn, 1 hoofdletter bevatten en 1 cijfer of speciaal teken."
        )
        String newPassword
) {}
