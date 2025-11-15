package dev.soumya.ProductServiceApr25.controller;

import dev.soumya.ProductServiceApr25.dto.CategoryRequestDTO;
import dev.soumya.ProductServiceApr25.dto.CategoryResponseDTO;
import dev.soumya.ProductServiceApr25.dto.ProductResponseDTO;
import dev.soumya.ProductServiceApr25.model.Category;
import dev.soumya.ProductServiceApr25.model.Product;
import dev.soumya.ProductServiceApr25.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        Category savedCategory = categoryService.createCategory(categoryRequestDTO);
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(savedCategory.getName(),
                savedCategory.getDescription());
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") int id) {
        Category fetchedCategory = categoryService.getCategory(id);
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(fetchedCategory.getName(),
                fetchedCategory.getDescription());
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();
        for(Category category : categories) {
            CategoryResponseDTO responseDTO = new CategoryResponseDTO(
                    category.getName(), category.getDescription()
            );
            categoryResponseDTOs.add(responseDTO);
        }
        return ResponseEntity.ok(categoryResponseDTOs);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("id") int id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @GetMapping("/product/category/{id}")
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsByCategory(@PathVariable("id") int categoryId) {
        List<Product> fetchedProducts = categoryService.getAllProductsByCategory(categoryId);
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        for(Product product : fetchedProducts) {
            ProductResponseDTO responseDTO = new ProductResponseDTO(
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getRating()
            );

            productResponseDTOs.add(responseDTO);
        }
        return ResponseEntity.ok(productResponseDTOs);
    }

//    @GetMapping("/category/product/{id}")
//    public ResponseEntity<CategoryResponseDTO> getCategoryFromProduct(@PathVariable("id") int productId) {
//        Category category = categoryService.getCategoryFromProduct(productId);
//        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(
//            category.getName(), category.getDescription()
//        );
//        return ResponseEntity.ok(categoryResponseDTO);
//    }
}
