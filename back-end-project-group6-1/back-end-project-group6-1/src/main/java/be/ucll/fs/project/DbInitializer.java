package be.ucll.fs.project;

import java.util.List;

import be.ucll.fs.project.model.*;
import be.ucll.fs.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {
    private final CookbookRepository cookbookRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DbInitializer(CookbookRepository cookbookRepository,
                         RecipeRepository recipeRepository,
                         UserRepository userRepository,
                         IngredientRepository ingredientRepository,
                         ProfileRepository profileRepository,
                         PasswordEncoder passwordEncoder) {
        this.cookbookRepository = cookbookRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initialize() {
        User admin = new User("admin", "Super", "Admin", "admin@mycookbook.com", passwordEncoder.encode("admin123"), Role.ADMIN);
        User chefMario = new User("mario", "Mario", "Rossi", "mario@italy.com", passwordEncoder.encode("pasta"), Role.USER);
        User sarahFit = new User("sarah", "Sarah", "Fit", "sarah@health.com", passwordEncoder.encode("gymlife"), Role.USER);
        User user1 = new User("user1", "user", "1", "user1@ucll.be", passwordEncoder.encode("user1"), Role.USER);
        User user2 = new User("user2", "user", "2", "user2@ucll.be", passwordEncoder.encode("user2"), Role.ADMIN);
        User frans = new User("frans", "frans", "spaans", "frans@ucll.be", passwordEncoder.encode("frans123"), Role.USER);
        User frits = new User("frits", "frits", "frats", "frits@ucll.be", passwordEncoder.encode("frits123"), Role.ADMIN);

        userRepository.save(admin);
        userRepository.save(chefMario);
        userRepository.save(sarahFit);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(frans);
        userRepository.save(frits);

        Profile marioProfile = new Profile(chefMario, "Mario Rossi", "I am an Italian chef who loves pasta!");
        Profile sarahProfile = new Profile(sarahFit, "Sarah Fit", "Fitness enthusiast and healthy food lover.");
        Profile adminProfile = new Profile(admin, "Super Admin", "I manage this application.");
        Profile user1Profile = new Profile(user1, "User 1", "I am a regular user of this app.");
        Profile user2Profile = new Profile(user2, "User 2", "I manage this application.");
        Profile fransProfile = new Profile(frans, "Frans Spaans", "I am a regular user of this app. I also love specialties from different countries.");
        Profile fritsProfile = new Profile(frits, "Frits frats", "I manage this application.");

        profileRepository.save(marioProfile);
        profileRepository.save(sarahProfile);
        profileRepository.save(adminProfile);
        profileRepository.save(user1Profile);
        profileRepository.save(user2Profile);
        profileRepository.save(fransProfile);
        profileRepository.save(fritsProfile);

        Cookbook italianClassics = new Cookbook("Italian Classics", 0, chefMario);
        Cookbook pizzaSecrets = new Cookbook("Pizza Secrets", 0, chefMario);
        Cookbook healthyBreakfasts = new Cookbook("Healthy Breakfasts", 0, sarahFit);
        Cookbook BelgiumsBest = new Cookbook("Belgiums Best", 0, user2);

        cookbookRepository.save(italianClassics);
        cookbookRepository.save(pizzaSecrets);
        cookbookRepository.save(healthyBreakfasts);
        cookbookRepository.save(BelgiumsBest);

        Recipe carbonara = new Recipe("Spaghetti Carbonara", 15, 20);
        Recipe bolo = new Recipe("Spaghetti Bolognese", 20, 120);
        Recipe pastapesto = new Recipe("Pasta Pesto", 10, 10);

        italianClassics.addRecipe(carbonara);
        italianClassics.addRecipe(bolo);
        italianClassics.addRecipe(pastapesto);

        Recipe oatmeal = new Recipe("Oats & Berries", 5, 5);
        Recipe smoothie = new Recipe("Green Smoothie", 5, 0);

        healthyBreakfasts.addRecipe(oatmeal);
        healthyBreakfasts.addRecipe(smoothie);

        Ingredient spaghetti = new Ingredient("spaghetti");
        Ingredient bacon = new Ingredient("bacon");
        Ingredient eggs = new Ingredient("eggs");
        Ingredient parmesan = new Ingredient("parmesan");
        Ingredient blackPepper = new Ingredient("black pepper");
        Ingredient garlic = new Ingredient("garlic");
        Ingredient milk = new Ingredient("milk");

        Ingredient mincedMeat = new Ingredient("minced meat");
        Ingredient tomatoSauce = new Ingredient("tomato sauce");
        Ingredient onion = new Ingredient("onion");
        Ingredient carrot = new Ingredient("carrot");

        Ingredient pasta = new Ingredient("pasta");
        Ingredient pesto = new Ingredient("pesto");
        Ingredient pineNuts = new Ingredient("pine nuts");
        Ingredient basil = new Ingredient("basil");

        Ingredient oats = new Ingredient("oats");
        Ingredient blueberries = new Ingredient("blueberries");
        Ingredient strawberries = new Ingredient("strawberries");
        Ingredient honey = new Ingredient("honey");

        Ingredient banana = new Ingredient("banana");
        Ingredient spinach = new Ingredient("spinach");
        Ingredient appleJuice = new Ingredient("apple juice");
        Ingredient avocado = new Ingredient("avocado");


        carbonara.addIngredient(spaghetti);
        carbonara.addIngredient(bacon);
        carbonara.addIngredient(eggs);
        carbonara.addIngredient(parmesan);
        carbonara.addIngredient(blackPepper);
        carbonara.addIngredient(garlic);
        carbonara.addIngredient(milk);

        bolo.addIngredient(spaghetti);
        bolo.addIngredient(mincedMeat);
        bolo.addIngredient(tomatoSauce);
        bolo.addIngredient(onion);
        bolo.addIngredient(garlic);
        bolo.addIngredient(carrot);

        pastapesto.addIngredient(pasta);
        pastapesto.addIngredient(pesto);
        pastapesto.addIngredient(pineNuts);
        pastapesto.addIngredient(basil);
        pastapesto.addIngredient(parmesan);

        oatmeal.addIngredient(oats);
        oatmeal.addIngredient(milk);
        oatmeal.addIngredient(blueberries);
        oatmeal.addIngredient(strawberries);
        oatmeal.addIngredient(honey);
        oatmeal.addIngredient(banana);

        smoothie.addIngredient(banana);
        smoothie.addIngredient(spinach);
        smoothie.addIngredient(appleJuice);
        smoothie.addIngredient(avocado);
        smoothie.addIngredient(honey);

        ingredientRepository.saveAll(List.of(
            spaghetti, 
            bacon, 
            eggs, 
            parmesan, 
            blackPepper, 
            garlic, 
            milk, 
            mincedMeat, 
            tomatoSauce, 
            onion,
            carrot, 
            pasta, 
            pesto, 
            pineNuts, 
            basil, 
            oats, 
            blueberries, 
            strawberries, 
            honey, 
            banana, 
            spinach, 
            appleJuice, 
            avocado
        ));

        recipeRepository.save(carbonara);
        recipeRepository.save(bolo);
        recipeRepository.save(pastapesto);
        recipeRepository.save(oatmeal);
        recipeRepository.save(smoothie);

        cookbookRepository.save(italianClassics);
        cookbookRepository.save(healthyBreakfasts);
    }
}