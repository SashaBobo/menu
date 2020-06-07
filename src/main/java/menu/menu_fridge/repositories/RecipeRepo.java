package menu.menu_fridge.repositories;

import menu.menu_fridge.models.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepo extends CrudRepository<Recipe, Long> {

    List<Recipe> findByTitle(String title);

    Optional<Recipe> findById(Long id);
}
