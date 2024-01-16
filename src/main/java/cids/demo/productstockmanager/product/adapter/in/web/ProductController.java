package cids.demo.productstockmanager.product.adapter.in.web;

import cids.demo.productstockmanager.product.application.port.in.*;
import cids.demo.productstockmanager.product.application.service.ProductNotFoundException;
import cids.demo.productstockmanager.product.domain.Product;
import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("products")
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final AddProductUseCase addProductUseCase;
    private final GetProductsUseCase getProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(AddProductUseCase addProductUseCase,
                             GetProductsUseCase getProductsUseCase,
                             UpdateProductUseCase updateProductUseCase,
                             DeleteProductUseCase deleteProductUseCase
    ) {
        this.addProductUseCase = addProductUseCase;
        this.getProductsUseCase = getProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        LOGGER.info("Received get all products request");
        var products = getProductsUseCase.getAllProducts();
        LOGGER.info("Get all products request completed successfully");
        return products;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        LOGGER.info("Received get product (ID: {}) request", id);
        var product = getProductsUseCase.getProduct(id).orElseThrow(() -> {
            var err = String.format("Product with ID '%d' not found.", id);
            LOGGER.error(err);
            return new ResourceNotFoundException(err);
        });
        LOGGER.info("Get product (ID: {}) request completed successfully", id);
        return product;
    }

    @GetMapping("/supplier/{supplierId}")
    public List<Product> getProducts(@PathVariable Long supplierId) {
        LOGGER.info("Received get products by supplier (ID: {}) request", supplierId);
        var products = getProductsUseCase.getProductsBySupplier(supplierId);
        LOGGER.info("Get products by supplier (ID: {}) request completed successfully", supplierId);
        return products;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        LOGGER.info("Received delete product (ID: {}) request", id);
        deleteProductUseCase.deleteProduct(id);
        LOGGER.info("Delete product (ID: {}) request completed successfully", id);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDto productInfo) {
        LOGGER.info("Received create product request. Details: {}", productInfo);
        try {
            var newProductId = addProductUseCase.addProduct(productInfo.name(), productInfo.quantity(), productInfo.supplierId()).getId();
            LOGGER.info("Create product request completed successfully");
            return ResponseEntity.created(URI.create("/products/" + newProductId)).build();
        } catch (SupplierNotFoundException ex) {
            LOGGER.error("Create product request failed");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productInfo) {
        LOGGER.info("Received update product request for product ID {}. Details: {}", id, productInfo);
        try {
            updateProductUseCase.updateProduct(id, productInfo);
            LOGGER.info("Update product request completed successfully");
        } catch (SupplierNotFoundException | ProductNotFoundException ex) {
            LOGGER.error("Update product request failed");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @ExceptionHandler
    public ErrorResponse handleInputValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)).property("fieldErrors", fieldErrors).build();
    }
}
