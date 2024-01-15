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
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        return getProductsUseCase.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return getProductsUseCase.getProduct(id).orElseThrow(() -> {
            String err = String.format("Product with ID '%d' not found.", id);
            LOGGER.error(err);
            return new ResourceNotFoundException(err);
        });
    }

    @GetMapping("/supplier/{name}")
    public List<Product> getProducts(@PathVariable Long id) {
        return getProductsUseCase.getProductsBySupplier(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.deleteProduct(id);
    }

    @PostMapping
    public Product addProduct(@Valid @RequestBody ProductDto productInfo) {
        try {
            return addProductUseCase.addProduct(productInfo.name(), productInfo.quantity(), productInfo.supplierId());
        } catch (SupplierNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productInfo) {
        try {
            return updateProductUseCase.updateProduct(id, productInfo);
        } catch (SupplierNotFoundException | ProductNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @ExceptionHandler
    public ErrorResponse handleInputValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)).property("fieldErrors", fieldErrors).build();
    }
}
