package menu.menu_fridge.repositories;

import menu.menu_fridge.models.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product, Long> {

    List<Product> findByProductName(String productName);

    Object findAll(Sort productName);
}
