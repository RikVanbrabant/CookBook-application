package be.ucll.fs.project.controller;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Family;
import be.ucll.fs.project.service.FamilyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/families")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    public record CreateFamilyRequest(@NotBlank String name) {}

    public record AddMemberRequest(@NotBlank String username) {}

    @GetMapping
    public List<Family> getMyFamilies(Authentication authentication) {
        return familyService.getVisibleFamilies(authentication.getName());
    }


    @GetMapping("/{id}")
    public Family getFamily(@PathVariable Long id, Authentication authentication) {
        return familyService.getFamily(id, authentication.getName());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Family createFamily(@Valid @RequestBody CreateFamilyRequest body,
                               Authentication authentication) {
        return familyService.createFamily(body.name(), authentication.getName());
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Family addMember(@PathVariable Long id,
                            @Valid @RequestBody AddMemberRequest body,
                            Authentication authentication) {
        return familyService.addMember(id, body.username(), authentication.getName());
    }

    @DeleteMapping("/{id}/members/{username}")
    public Family removeMember(@PathVariable Long id,
                               @PathVariable String username,
                               Authentication authentication) {
        return familyService.removeMember(id, username, authentication.getName());
    }

    @GetMapping("/{id}/cookbooks")
    public List<Cookbook> getFamilyCookbooks(@PathVariable Long id,
                                             Authentication authentication) {
        return familyService.getFamilyCookbooks(id, authentication.getName());
    }

    @PostMapping("/{id}/cookbooks/{cookbookId}")
    public Cookbook shareCookbook(@PathVariable Long id,
                                  @PathVariable Long cookbookId,
                                  Authentication authentication) {
        return familyService.shareCookbookWithFamily(id, cookbookId, authentication.getName());
    }
}
