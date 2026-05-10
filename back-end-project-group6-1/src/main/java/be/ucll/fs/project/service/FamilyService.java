package be.ucll.fs.project.service;

import be.ucll.fs.project.exception.CoursesException;
import be.ucll.fs.project.exception.NotFoundException;
import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Family;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.CookbookRepository;
import be.ucll.fs.project.repository.FamilyRepository;
import be.ucll.fs.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final CookbookRepository cookbookRepository;

    public FamilyService(FamilyRepository familyRepository,
                         UserRepository userRepository,
                         CookbookRepository cookbookRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
        this.cookbookRepository = cookbookRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }

    private Family getFamilyOrThrow(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new NotFoundException("Family not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    public Family createFamily(String name, String creatorUsername) {
        User creator = getUserByUsername(creatorUsername);
        Family family = new Family(name, creator);
        return familyRepository.save(family);
    }

    public List<Family> getVisibleFamilies(String username) {
        User caller = getUserByUsername(username);
        if (isAdmin(caller)) {
            return familyRepository.findAll();
        }
        return familyRepository.findByMembersContaining(caller);
    }

    public Family getFamily(Long familyId, String username) {
        User caller = getUserByUsername(username);
        Family family = getFamilyOrThrow(familyId);

        if (!isAdmin(caller) && !family.hasMember(caller)) {
            throw new AccessDeniedException("You are not a member of this family.");
        }
        return family;
    }

    public Family addMember(Long familyId, String usernameToAdd, String callerUsername) {
        User caller = getUserByUsername(callerUsername);
        Family family = getFamilyOrThrow(familyId);

        if (!isAdmin(caller) && !family.isHeadOfFamily(caller)) {
            throw new AccessDeniedException(
                    "Only the head of family or an admin can add members.");
        }

        User newMember = getUserByUsername(usernameToAdd);
        if (family.hasMember(newMember)) {
            throw new CoursesException(usernameToAdd + " is already a member of this family.");
        }
        family.addMember(newMember);
        return familyRepository.save(family);
    }

    public Family removeMember(Long familyId, String usernameToRemove, String callerUsername) {
        User caller = getUserByUsername(callerUsername);
        Family family = getFamilyOrThrow(familyId);

        if (!isAdmin(caller) && !family.isHeadOfFamily(caller)) {
            throw new AccessDeniedException(
                    "Only the head of family or an admin can remove members.");
        }

        User toRemove = getUserByUsername(usernameToRemove);
        if (family.isHeadOfFamily(toRemove)) {
            throw new CoursesException(
                    "The head of family cannot be removed. Transfer headship first.");
        }
        if (!family.hasMember(toRemove)) {
            throw new CoursesException(usernameToRemove + " is not a member of this family.");
        }
        family.removeMember(toRemove);
        return familyRepository.save(family);
    }

    public List<Cookbook> getFamilyCookbooks(Long familyId, String callerUsername) {
        User caller = getUserByUsername(callerUsername);
        Family family = getFamilyOrThrow(familyId);

        if (!isAdmin(caller) && !family.hasMember(caller)) {
            throw new AccessDeniedException("You are not a member of this family.");
        }
        return cookbookRepository.findByFamilyId(familyId);
    }

    public Cookbook shareCookbookWithFamily(Long familyId, Long cookbookId, String callerUsername) {
        User caller = getUserByUsername(callerUsername);
        Family family = getFamilyOrThrow(familyId);

        Cookbook cookbook = cookbookRepository.findById(cookbookId)
                .orElseThrow(() -> new NotFoundException("Cookbook not found"));

        boolean isOwner = cookbook.getUser() != null
                && cookbook.getUser().getUsername().equals(callerUsername);

        if (!isAdmin(caller) && !isOwner) {
            throw new AccessDeniedException(
                    "You can only share cookbooks you own.");
        }
        if (!isAdmin(caller) && !family.hasMember(caller)) {
            throw new AccessDeniedException("You are not a member of this family.");
        }

        cookbook.setFamily(family);
        return cookbookRepository.save(cookbook);
    }
}
