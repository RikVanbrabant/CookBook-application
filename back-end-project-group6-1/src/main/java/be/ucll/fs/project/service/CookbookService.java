package be.ucll.fs.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.repository.CookbookRepository;

@Service
public class CookbookService {
    private CookbookRepository cookbookRepository;

    public CookbookService(CookbookRepository cookbookRepository) {
        this.cookbookRepository = cookbookRepository;
    }

    public List<Cookbook> getAllCookbooks() {
        return cookbookRepository.getAllCookbooks();
    }

    public Cookbook getCookbookById(Long id) {
        return cookbookRepository.getCookbookById(id);
    }
}
