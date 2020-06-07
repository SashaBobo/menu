package menu.menu_fridge.controllers;

import menu.menu_fridge.models.Category;
import menu.menu_fridge.models.Product;
import menu.menu_fridge.models.Recipe;
import menu.menu_fridge.models.User;
import menu.menu_fridge.repositories.RecipeRepo;
import menu.menu_fridge.service.ChooseService;
import menu.menu_fridge.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class RecipeController {
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private ChooseService chooseService;
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes")
    public String getRecipe(@RequestParam(required = false, defaultValue = "") String filter,
                            @AuthenticationPrincipal User user, Model model) {
        Iterable<Recipe> recipes;
        if (filter != null && !filter.isEmpty()) {
            recipes = recipeRepo.findByTitle(filter);
        } else recipes = recipeRepo.findAll();
        model.addAttribute("user", user);
        model.addAttribute("filter", filter);
        model.addAttribute("recipe", recipes);
        model.addAttribute("categories", Category.values());
        return "recipes";
    }

    @PostMapping("/recipes/all")
    public String chooseAll() {
        return "redirect:/recipes";
    }

    @PostMapping("/recipes")
    public String chooseCategory(@RequestParam(name = "category", required = false, defaultValue = "") Category category,
                                 @RequestParam("user") User user, Model model) {
        List<Recipe> recipes;
        if (category != null) {
            recipes = chooseService.chooseRecipesForCategories(category, (List<Recipe>) recipeRepo.findAll());
        } else recipes = chooseService.chooseRecipesForAuthor(user, (List<Recipe>) recipeRepo.findAll());
        model.addAttribute("user", user);
        model.addAttribute("recipe", recipes);
        model.addAttribute("categories", Category.values());
        return "recipes";
    }

    @PostMapping("/recipes/delete")
    public String deleteRecipe(@RequestParam("recipeId") Long id) {
        Optional<Recipe> recipe = recipeRepo.findById(id);
        recipeRepo.delete(recipe.get());
        return "redirect:/recipes";
    }

    @GetMapping("/editRecipe/{recipe}")
    public String getEditRecipe(@PathVariable Recipe recipe,
                                @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("recipeEdit", recipe);
        model.addAttribute("product", recipeService.getProducts());
        model.addAttribute("categories", Category.values());
        return "editRecipe";
    }

    @PostMapping("/editRecipe/{recipe}")
    public String saveEditRecipe(@PathVariable Recipe recipe,
                                 @RequestParam("title") String title,
                                 @RequestParam("instruction") String instruction,
                                 @RequestParam(name = "category", required = false, defaultValue = "") Category category,
                                 @RequestParam(name = "product", required = false, defaultValue = "") List<Product> products,
                                 @RequestParam("file") MultipartFile file, Model model) throws IOException {
        recipeService.updateRecipe(recipe.getId(), title, instruction, category, products, file);
        model.addAttribute("recipeEdit", recipe);
        return "redirect:/recipes";
    }

    @GetMapping("/newRecipe")
    public String getProduct(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("product", recipeService.getProducts());
        model.addAttribute("categories", Category.values());
        return "newRecipe";
    }

    @PostMapping("/newRecipe")
    public String addRecipe(@AuthenticationPrincipal User user,
                            @RequestParam String title,
                            @RequestParam String instruction,
                            @RequestParam("category") Category category,
                            @RequestParam(name = "productR", required = false, defaultValue = "") List<Product> products,
                            @RequestParam("file") MultipartFile file) throws IOException {
        if (!title.isEmpty() && !instruction.isEmpty()) {
            recipeRepo.save(recipeService.addRecipe(user, title, instruction, category, products, file));
        }
        return "redirect:/recipes";
    }

    @PostMapping("/newRecipe/add")
    public String addProduct(@RequestParam String productName,
                             Model model) {
        recipeService.addProduct(productName);
        model.addAttribute("product", recipeService.getProducts());
        return "redirect:/newRecipe";
    }
}