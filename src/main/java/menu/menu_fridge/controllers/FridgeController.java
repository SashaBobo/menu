package menu.menu_fridge.controllers;

import menu.menu_fridge.models.Product;
import menu.menu_fridge.models.User;
import menu.menu_fridge.repositories.UserRepo;
import menu.menu_fridge.service.RecipeService;
import menu.menu_fridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/fridge")
public class FridgeController {
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String getProductFridge(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("products", recipeService.getProductsInFridge(user));
        model.addAttribute("user", user);
        return "fridge";
    }

    @PostMapping
    public String deleteProduct(@RequestParam(name = "product", required = false) Product product,
                                @RequestParam("user") User user, Model model) {
        if (product != null) {
            user.getProductsInFridge().remove(product);
            userRepo.save(user);
            model.addAttribute("products",  recipeService.getProductsInFridge(user));
        }
        return "redirect:/fridge";
    }
}
