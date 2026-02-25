package be.ucll.se.courses.backend.unit.service;

import be.ucll.fs.project.model.Ingredient;
import be.ucll.fs.project.repository.IngredientRepository;
import be.ucll.fs.project.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void getAllIngredients_shouldReturnList() {
        Ingredient salt = new Ingredient("Salt");
        when(ingredientRepository.findAll()).thenReturn(List.of(salt));

        List<Ingredient> result = ingredientService.getAllIngredients();

        assertEquals(1, result.size());
        assertEquals("Salt", result.get(0).getName());
        verify(ingredientRepository).findAll();
    }
}