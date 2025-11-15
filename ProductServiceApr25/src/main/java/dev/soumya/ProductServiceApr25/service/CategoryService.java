package dev.soumya.ProductServiceApr25.service;

import dev.soumya.ProductServiceApr25.dto.CategoryRequestDTO;
import dev.soumya.ProductServiceApr25.exception.DuplicateCategoryNameException;
import dev.soumya.ProductServiceApr25.exception.CategoryNotFoundException;
import dev.soumya.ProductServiceApr25.model.Category;
import dev.soumya.ProductServiceApr25.model.Product;
import dev.soumya.ProductServiceApr25.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;

    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryRequestDTO.getCategoryName());
        if(categoryOptional.isPresent()) {
            throw new DuplicateCategoryNameException("Duplicate category name " +
                    categoryRequestDTO.getCategoryName());
        }

        Category category = new Category();
        category.setName(categoryRequestDTO.getCategoryName());
        category.setDescription(categoryRequestDTO.getCategoryDescription());
        return categoryRepository.save(category);
    }

    public Category getCategory(int categoryId) {
        Category fetchedCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("Category not found with Id " + categoryId)
        );
        return fetchedCategory;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> getAllProductsByCategory(int categoryId) {
        Category fetchedCategory = getCategory(categoryId);
        List<Product> products = fetchedCategory.getProducts();
        return products;
    }

    public boolean deleteCategory(int categoryId) {
        Category category = getCategory(categoryId);
        //since fetch type for category is eager, so fetching category would also pull all
        //the products under this category
        for(Product product : category.getProducts()) {
            productService.deleteProductById(product.getId());
        }
        categoryRepository.deleteById(categoryId);
        return true;
    }

//    public Category getCategoryFromProduct(int productId) {
//        Product product = productService.getProduct(productId);
//        Category category = categoryRepository.findByProductsIn(List.of(product)).orElseThrow(
//                () -> new CategoryNotFoundException("Category not found")
//        );
//        return category;
//    }
}
