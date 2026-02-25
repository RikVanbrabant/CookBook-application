package be.ucll.se.courses.backend.unit.service;

import be.ucll.fs.project.exception.NotFoundException;
import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.repository.CookbookRepository;
import be.ucll.fs.project.service.CookbookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CookbookServiceTest {

    @Mock
    private CookbookRepository cookbookRepository;

    @InjectMocks
    private CookbookService cookbookService;

    @Test
    void getAllCookbooks_shouldReturnAllCookbooks() {
        Cookbook cb1 = new Cookbook("Italian", 5);
        Cookbook cb2 = new Cookbook("French", 2);
        List<Cookbook> expectedCookbooks = Arrays.asList(cb1, cb2);

        when(cookbookRepository.findAll()).thenReturn(expectedCookbooks);

        List<Cookbook> result = cookbookService.getAllCookbooks();

        assertEquals(2, result.size());
    }

    @Test
    void getCookbookById_shouldReturnCookbookIfExists() {
        Long cookbookId = 1L;
        Cookbook expectedCookbook = new Cookbook("Mexican", 10);

        when(cookbookRepository.findById(cookbookId)).thenReturn(Optional.of(expectedCookbook));

        Cookbook result = cookbookService.getCookbookById(cookbookId);

        assertNotNull(result);
        assertEquals("Mexican", result.getTitle());
    }

    @Test
    void getCookbookById_shouldThrowExceptionIfNotExists() {
        Long nonExistentId = 99L;

        when(cookbookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            cookbookService.getCookbookById(nonExistentId);
        });
    }
}