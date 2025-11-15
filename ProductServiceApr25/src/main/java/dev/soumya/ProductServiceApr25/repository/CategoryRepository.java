package dev.soumya.ProductServiceApr25.repository;

import dev.soumya.ProductServiceApr25.model.Category;
import dev.soumya.ProductServiceApr25.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    /*
    Category contains a list of Products so ProductsIn help us to pass 1 or even more than 1
    product to get the category for those products. It is similar to the IN keyword in SQL.
     */
    Optional<Category> findByProductsIn(List<Product> products);
}
