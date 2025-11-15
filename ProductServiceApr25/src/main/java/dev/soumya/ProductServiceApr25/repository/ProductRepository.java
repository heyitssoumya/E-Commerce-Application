package dev.soumya.ProductServiceApr25.repository;

import dev.soumya.ProductServiceApr25.dto.ProductProjection;
import dev.soumya.ProductServiceApr25.model.Category;
import dev.soumya.ProductServiceApr25.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /*
    JpaRepository takes the table for which Im creating the repo and the type of
    primary key in Product
    JpaRepository has the methods for CRUD operations, so extending JpaRepository
    all fundamental CRUD operation methods in Product interface, we dont need implement
    those methods, spring data jpa will do that for us, we will just use them directly
     */

    Page<Product> findAll(Pageable pageable);

    //shortcut to get all products with a specific description
    //custom method
    List<Product> findAllByDescription(String description);
    List<Product> findAllByDescriptionIgnoreCase(String description);
    /*
    Projection: fetching only required details
     */
    ProductProjection findFirstByName(String name);
    /*
    Product model does not contain categoryId, so we cannot query using categoryId.
    Even though categoryId is present as a foreign key in Product table, it is not an
    attribute of Product model that we have created. JPA only deals with model level
    and not DB level.
     */



}
