package menu.menu_fridge.service;

import menu.menu_fridge.models.Category;
import menu.menu_fridge.models.Product;
import menu.menu_fridge.models.Recipe;
import menu.menu_fridge.models.User;
import menu.menu_fridge.repositories.RecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChooseService {
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private UserService userService;

    public List<Recipe> chooseRecipesForProducts() {
        User user = userService.getCurrentUser();
        List<Product> productsInFridge = user.getProductsInFridge();
        List<Product> productsInRecipe;
        ArrayList<Recipe> recipe = (ArrayList<Recipe>) recipeRepo.findAll();
        ArrayList<Recipe> recipes = new ArrayList<>();
        for (Recipe r : recipe) {
            productsInRecipe = r.getProductsInRecipe();
            if (productsInRecipe != null && !productsInRecipe.isEmpty() &&
                    productsInFridge != null && !productsInFridge.isEmpty()) {
                if (productsInFridge.containsAll(productsInRecipe)) {
                    recipes.add(r);
                }
            }
        }
        return recipes;
    }

    public List<Recipe> chooseRecipesForCategories(Category category, List<Recipe> recipe) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        for (Category c : Category.values()) {
            if (category != null && c.name() == category.name()) {
                for (Recipe r : recipe) {
                    if (r.getCategory() != null && r.getCategory().name() == category.name()) {
                        recipes.add(r);
                    }
                }
            }
        }
        return recipes;
    }

    public List<Recipe> chooseRecipesForAuthor(User user, List<Recipe> recipe) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        for (Recipe r : recipe) {
            if (r.getAuthor() != null &&
                    r.getAuthorName() == user.getUsername() &&
                    r.getAuthor().getId() == user.getId()) {
                recipes.add(r);
            }
        }
        return recipes;
    }

}
