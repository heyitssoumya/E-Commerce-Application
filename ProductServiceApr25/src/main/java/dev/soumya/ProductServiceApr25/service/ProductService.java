package dev.soumya.ProductServiceApr25.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soumya.ProductServiceApr25.client.FakeStoreClient;
import dev.soumya.ProductServiceApr25.dto.FakeStoreProductDTO;
import dev.soumya.ProductServiceApr25.dto.ProductProjection;
import dev.soumya.ProductServiceApr25.dto.ProductRequestDTO;
import dev.soumya.ProductServiceApr25.exception.CategoryNotFoundException;
import dev.soumya.ProductServiceApr25.exception.ProductNotFoundException;
import dev.soumya.ProductServiceApr25.model.Category;
import dev.soumya.ProductServiceApr25.model.Product;
import dev.soumya.ProductServiceApr25.repository.CategoryRepository;
import dev.soumya.ProductServiceApr25.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private FakeStoreClient fakeStoreClient;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    /*
    using categoryService instead of category repository as category service handles exception as
    well in case category with particular id does not exist
     */
//    @Autowired
//    private CategoryService categoryService;

    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Category savedCategory = categoryRepository.findById(productRequestDTO.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException("Category does not exist!")
        );


        Product product = new Product();

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setRating(productRequestDTO.getRating());
        product.setQuantity(productRequestDTO.getQuantity());

        Product savedProduct = productRepository.save(product);
        savedCategory.getProducts().add(product);
        categoryRepository.save(savedCategory);

        return savedProduct;
    }


    public Product getProductById(int productId) {
        String key = "product:" + productId;

        Object cachedObj = redisTemplate.opsForValue().get(key);
        if (cachedObj != null) {
            // Convert LinkedHashMap to Product
            Product cachedProduct = objectMapper.convertValue(cachedObj, Product.class);
            return cachedProduct;
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + productId + " not found");
        }

        Product product = productOptional.get();

        redisTemplate.opsForValue().set(key, product, 10, TimeUnit.MINUTES);
        return product;

    }

    public Page<Product> getAllProducts(int pageNumber, int pageSize){
        Sort sort = Sort.by("name").ascending();

        return productRepository.findAll(
                PageRequest.of(pageNumber, pageSize, sort)
        );
    }

    public Product updateProduct(Product newProduct, int productId) {
        Product savedProduct = getProductById(productId);
        newProduct.setId(productId);
        Product updatedProduct = productRepository.save(newProduct);
        return updatedProduct;
    }

    public Boolean deleteProductById(int productId) {
        productRepository.deleteById(productId);
        return true;
    }

    public List<Product> getProductByCategoryId(int categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("Category does not exist!")
        );
        return savedCategory.getProducts();
    }

//   public List<Product> getProductByDescription(String description) {
//        List<Product> allProducts = productRepository.findAll();
//        List<Product> productWithGivenDescription = new ArrayList<>();
//        for(Product p : allProducts) {
//            if(p.getDescription().equals(description)) {
//                productWithGivenDescription.add(p);
//            }
//        }
//        return productWithGivenDescription;
//    }

    //Alternative for above method
    public List<Product> getProductByDescription(String description) {
        List<Product> matchedProducts =
                productRepository.findAllByDescriptionIgnoreCase(description);
        return matchedProducts;
    }

    public ProductProjection getProductProjection(String productName) {
        return productRepository.findFirstByName(productName);
    }





    public FakeStoreProductDTO[] getAllFakeStoreProducts() {
        return fakeStoreClient.getAllProducts();
    }

    public FakeStoreProductDTO getFakeStoreProductById(int productId) {
        return fakeStoreClient.getProduct(productId);
    }

    public FakeStoreProductDTO createFakeStoreProduct(FakeStoreProductDTO input) {
        return fakeStoreClient.createProduct(input);
    }

    public FakeStoreProductDTO replaceFakeStoreProduct(int productId, FakeStoreProductDTO input) {
        return fakeStoreClient.replaceProduct(productId, input);
    }

    public Boolean deleteFakeStoreProduct(int productId) {
        return fakeStoreClient.deleteProductFromFakeStore(productId);
    }
}
