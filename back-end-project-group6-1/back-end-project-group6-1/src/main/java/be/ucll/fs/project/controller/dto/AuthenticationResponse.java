package be.ucll.fs.project.controller.dto;

import be.ucll.fs.project.model.Role;

public record AuthenticationResponse(
        String message,
        String token,
        String username,
        String fullname,
        Role role
) {
}
