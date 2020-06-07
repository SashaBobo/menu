package menu.menu_fridge.controllers;

import menu.menu_fridge.models.Category;
import menu.menu_fridge.models.Recipe;
import menu.menu_fridge.models.User;
import menu.menu_fridge.service.ChooseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cooking")
public class CookController {
    @Autowired
    private ChooseService chooseService;

    @GetMapping
    public String cooking(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("recipe", chooseService.chooseRecipesForProducts());
        model.addAttribute("categories", Category.values());
        return "cooking";
    }

    @PostMapping("/all")
    public String chooseAll() {
        return "redirect:/cooking";
    }

    @PostMapping
    public String chooseCategory(@RequestParam(name = "category", required = false, defaultValue = "") Category category,
                                 @RequestParam("user") User user, Model model) {
        List<Recipe> recipes;
        if (category != null) {
            recipes = chooseService.chooseRecipesForCategories(category, chooseService.chooseRecipesForProducts());
        } else recipes = chooseService.chooseRecipesForAuthor(user, chooseService.chooseRecipesForProducts());
        model.addAttribute("user", user);
        model.addAttribute("recipe", recipes);
        model.addAttribute("categories", Category.values());
        return "cooking";
    }
}
