package be.ucll.fs.project.controller.dto;

import be.ucll.fs.project.model.Role;
import jakarta.validation.constraints.Email;

public record UserPatchRequest(
        String firstName,
        String lastName,
        @Email String email,
        Role role
) {}
