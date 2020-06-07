package menu.menu_fridge.service;

import menu.menu_fridge.models.Category;
import menu.menu_fridge.models.Product;
import menu.menu_fridge.models.Recipe;
import menu.menu_fridge.models.User;
import menu.menu_fridge.repositories.ProductRepo;
import menu.menu_fridge.repositories.RecipeRepo;
import menu.menu_fridge.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class RecipeService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RecipeRepo recipeRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Product> getProducts() {
        List<Product> product = (List<Product>) productRepo.findAll(Sort.by(Sort.Direction.ASC, "productName"));
        return product;
    }

    public List<Product> getProductsInFridge(User user) {
        List<Product> products = user.getProductsInFridge();
        products.sort(Comparator.comparing(p -> p.getProductName()));
        return products;
    }

    public void addProduct(String productName) {
        if (productName != null && !productName.isEmpty()) {
            List<Product> product = productRepo.findByProductName(productName);
            if (product == null || product.isEmpty()) {
                productRepo.save(new Product(productName));
            }
        }
    }

    public void productSaveInFridge(User user, List<Product> products) {
        List<Product> listRemove = new ArrayList<>();
        for (Product product : products) {
            for (Product productF : user.getProductsInFridge()) {
                if (productF.getId() == product.getId()) {
                    listRemove.add(product);
                }
            }
        }
        products.removeAll(listRemove);
        if (products != null && !products.isEmpty()) {
            user.getProductsInFridge().addAll(products);
            userRepo.save(user);
        }
    }

    public String addFileName(MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFilename));
        return resultFilename;
    }

    public Recipe addRecipe(User user, String title, String instruction, Category category,
                            List<Product> products, MultipartFile file) throws IOException {
        Recipe newRecipe = new Recipe(title, instruction, Category.valueOf(category.name()));
        if (products != null || !products.isEmpty()) {
            newRecipe.setProductsInRecipe(products);
        }
        if (user != null) {
            newRecipe.setAuthor(user);
        }
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            newRecipe.setFilename(addFileName(file));
        }
        return newRecipe;
    }

    public void updateRecipe(Long recipeId, String title, String instruction, Category category,
                             List<Product> products, MultipartFile file) throws IOException {
        Optional<Recipe> recipe = recipeRepo.findById(recipeId);
        Recipe r = recipe.get();
        if (title != null && !title.isEmpty()) r.setTitle(title);
        if (instruction != null && !instruction.isEmpty()) r.setInstruction(instruction);
        if (category != null) r.setCategory(category);
        if (products != null && !products.isEmpty()) r.setProductsInRecipe(products);
        if (file != null && !file.getOriginalFilename().isEmpty()) r.setFilename(addFileName(file));
        recipeRepo.save(r);
    }
}
