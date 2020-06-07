package menu.menu_fridge.controllers;

import menu.menu_fridge.models.Product;
import menu.menu_fridge.models.User;
import menu.menu_fridge.repositories.ProductRepo;
import menu.menu_fridge.service.RecipeService;
import menu.menu_fridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getProduct(@RequestParam(required = false, defaultValue = "") String filter,
                             @AuthenticationPrincipal User user, Model model) {
        List<Product> product = recipeService.getProducts();
        if (filter != null && !filter.isEmpty()) {
            product = productRepo.findByProductName(filter);
        }
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("filter", filter);
        return "products";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String productName,
                             @AuthenticationPrincipal User user, Model model) {
        recipeService.addProduct(productName);
        model.addAttribute("user", user);
        model.addAttribute("product", recipeService.getProducts());
        return "products";
    }

    @PostMapping("/addFridge")
    public String productSaveInFridge(@RequestParam(name = "product", required = false, defaultValue = "") List<Product> products) {
        User user = userService.getCurrentUser();
        if (products != null && !products.isEmpty()) {
            recipeService.productSaveInFridge(user, products);
        }
        return "redirect:/fridge";
    }
}
