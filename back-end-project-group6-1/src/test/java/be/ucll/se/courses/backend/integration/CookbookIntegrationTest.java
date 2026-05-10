package be.ucll.se.courses.backend.integration;

import be.ucll.fs.project.Application;
import be.ucll.fs.project.controller.dto.RecipeInput;
import be.ucll.fs.project.model.Cookbook;
import be.ucll.fs.project.model.Recipe;
import be.ucll.fs.project.model.Role;
import be.ucll.fs.project.model.User;
import be.ucll.fs.project.repository.CookbookRepository;
import be.ucll.fs.project.repository.FamilyRepository;
import be.ucll.fs.project.repository.RecipeRepository;
import be.ucll.fs.project.repository.UserRepository;
import be.ucll.fs.project.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Transactional
class CookbookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CookbookRepository cookbookRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        cookbookRepository.deleteAll();
        familyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addRecipeToCookbook_shouldReturnCreatedRecipeAndPersistData_WhenOwner() throws Exception {
        User admin = new User("admin_int", "Ad", "Min", "admin_int@test.com", "pass", Role.ADMIN);
        userRepository.save(admin);

        String token = jwtService.generateToken(admin);
        Cookie authCookie = new Cookie("authToken", token);

        Cookbook cookbook = cookbookRepository.save(new Cookbook("Integration Test", 0, admin));
        Long cookbookId = cookbook.getId();

        RecipeInput input = new RecipeInput("Integration Pasta", 10, 30);

        mockMvc.perform(post("/cookbooks/" + cookbookId + "/recipes")
                        .cookie(authCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Pasta"));

        Cookbook updatedCookbook = cookbookRepository.findById(cookbookId).orElseThrow();
        assertEquals(1, updatedCookbook.getNumOfRecipes());
    }

    @Test
    void getRecipesByCookbookId_shouldReturnRecipes_WhenAuthenticated() throws Exception {
        User user = new User("user_int", "User", "Name", "user_int@test.com", "pass", Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        Cookie authCookie = new Cookie("authToken", token);

        Cookbook cb = cookbookRepository.save(new Cookbook("Test Recipes", 0, user));

        Recipe r1 = new Recipe("Soup", 5, 10);
        r1.setCookbook(cb);
        recipeRepository.save(r1);

        Recipe r2 = new Recipe("Salad", 2, 5);
        r2.setCookbook(cb);
        recipeRepository.save(r2);

        mockMvc.perform(get("/cookbooks/" + cb.getId() + "/recipes")
                        .cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Soup"));
    }

    @Test
    void getAllCookbooks_shouldReturnAllCookbooks_WhenAuthenticated() throws Exception {
        User user = new User("user_all", "User", "Name", "user_all@test.com", "pass", Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        Cookie authCookie = new Cookie("authToken", token);

        cookbookRepository.save(new Cookbook("Global Book 1", 1, user));
        cookbookRepository.save(new Cookbook("Global Book 2", 1, user));

        mockMvc.perform(get("/cookbooks")
                        .cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}