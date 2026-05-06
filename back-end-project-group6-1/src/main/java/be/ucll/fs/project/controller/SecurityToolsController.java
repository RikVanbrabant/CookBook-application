package be.ucll.fs.project.controller;

import be.ucll.fs.project.controller.dto.UrlValidationRequest;
import be.ucll.fs.project.service.UrlValidationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityToolsController {
    private final UrlValidationService urlValidationService;

    public SecurityToolsController(UrlValidationService urlValidationService) {
        this.urlValidationService = urlValidationService;
    }

    @PostMapping("/url-validate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public UrlValidationService.ValidationResult validateUrl(@Valid @RequestBody UrlValidationRequest request) {
        return urlValidationService.validate(request.url());
    }
}
