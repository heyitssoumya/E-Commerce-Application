package dev.soumya.ProductServiceApr25.controller;

import dev.soumya.ProductServiceApr25.dto.FakeStoreProductDTO;
import dev.soumya.ProductServiceApr25.dto.ProductProjection;
import dev.soumya.ProductServiceApr25.dto.ProductRequestDTO;
import dev.soumya.ProductServiceApr25.model.Product;
import dev.soumya.ProductServiceApr25.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product savedProduct = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    //GET http://localhost:8080/product?page=0&size=5
    @GetMapping("/product")
    public Page<Product> getAllProducts(
                    @RequestParam(defaultValue = "0") int pageNumber,
                    @RequestParam(defaultValue = "6") int pageSize) {
        return productService.getAllProducts(pageNumber, pageSize);

    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct ,@PathVariable("id") int id) {
        Product updatedProduct = productService.updateProduct(newProduct, id);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/product/{id}")
    public Boolean deleteProduct(@PathVariable("id") int id) {
        return productService.deleteProductById(id);
    }

//    @GetMapping("/product/category/{categoryId}")
//    public ResponseEntity<List<ProductResponseDTO>> getProductByCategory(@PathVariable("categoryId") int categoryId) {
//        List<Product> products = productService.getProductByCategory(categoryId);
//        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
//        for(Product product : products) {
//            ProductResponseDTO productResponseDTO = new ProductResponseDTO(
//                    product.getName(),
//                    product.getDescription(),
//                    product.getPrice(),
//                    product.getRating()
//            );
//            productResponseDTOS.add(productResponseDTO);
//        }
//        return ResponseEntity.ok(productResponseDTOS);
//    }



    //@GetMapping("product/{description}") and @GetMapping("/product/{id}") have
    //similar APIs, and dispatcher servlet can get confused so changing the API a
    //little bit
    @GetMapping("product/desc/{description}")
    public ResponseEntity<List<Product>> getProductByDescription(
            @PathVariable("description") String description) {
        List<Product> productWithGivenDescription = productService.getProductByDescription(description);
        return ResponseEntity.ok(productWithGivenDescription);
    }

    @GetMapping("product/projection/{name}")
    public ResponseEntity<ProductProjection> getFirstProductProjectionByName(
            @PathVariable("name") String name) {
        ProductProjection projection = productService.getProductProjection(name);
        return ResponseEntity.ok(projection);
    }



    @GetMapping("/product/fake")
    public FakeStoreProductDTO[] getAllFakeStoreProducts() {
        return productService.getAllFakeStoreProducts();
    }

/*
this API not only gives the body and http status code, but also a message indicating
what went wrong in case of an exception
 */
    @GetMapping("/product/fake/{id}")
    public ResponseEntity<FakeStoreProductDTO> getFakeStoreProductById(@PathVariable("id") int id){
        if(id <= 0 || id > 20){
            throw new IllegalArgumentException("Product doesn't exist");
        }
        FakeStoreProductDTO fakeStoreProductDTO = productService.getFakeStoreProductById(id);
        return new ResponseEntity<>(fakeStoreProductDTO, HttpStatus.OK);
    }

    @PostMapping("/product/fake")
    public FakeStoreProductDTO createFakeStoreProduct(@RequestBody FakeStoreProductDTO
    fakeStoreProductDTO) {
        return productService.createFakeStoreProduct(fakeStoreProductDTO);
    }

    @PutMapping("/product/fake/{id}")
    public FakeStoreProductDTO replaceFakeStoreProduct(@PathVariable("id") int id,
    @RequestBody FakeStoreProductDTO fakeStoreProductDTO) {
        return productService.replaceFakeStoreProduct(id, fakeStoreProductDTO);
    }

    @DeleteMapping("/product/fake/{id}")
    public Boolean deleteFakeStoreProduct(@PathVariable("id") int id) {
        return productService.deleteFakeStoreProduct(id);
    }
/*
This exception handler method specifically handles Illegal Argument exception
 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
