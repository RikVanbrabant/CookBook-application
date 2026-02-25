package be.ucll.fs.project.service;

import java.util.List;

import be.ucll.fs.project.controller.dto.CookbookInput;
import be.ucll.fs.project.exception.NotFoundException;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.repository.CookbookRepository;

@Service
@Transactional
public class CookbookService {
    private final CookbookRepository cookbookRepository;
    private final UserRepository userRepository;

    public CookbookService(CookbookRepository cookbookRepository, UserRepository userRepository) {
        this.cookbookRepository = cookbookRepository;
        this.userRepository = userRepository;
    }

    public List<Cookbook> getAllCookbooks() {
        return cookbookRepository.findAll();
    }

    public List<Cookbook> getCookbooksByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return cookbookRepository.findByUserId(user.getId());
    }

    public Cookbook getCookbookById(Long id) {
        return cookbookRepository.findById(id).orElseThrow(() -> new NotFoundException("Cookbook not found"));
    }
    public Cookbook createCookbook(CookbookInput cookbookInput, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Cookbook cookbook = new Cookbook(
                cookbookInput.title(),
                0,
                user
        );

        return cookbookRepository.save(cookbook);
    }
}