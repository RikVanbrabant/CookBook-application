package be.ucll.fs.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.fs.project.model.Ingredient;
import be.ucll.fs.project.repository.IngredientRepository;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
}
