package cids.demo.productstockmanager.product.adapter.in.web;

import cids.demo.productstockmanager.product.domain.Product;
import cids.demo.productstockmanager.product.application.port.in.ProductDto;
import cids.demo.productstockmanager.product.application.service.ProductService;
import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import jakarta.validation.Valid;
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
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id).get();
    }

    @GetMapping("/supplier/{name}")
    public List<Product> getProducts(@PathVariable Long id) {
        return productService.getProductsBySupplier(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping
    public Product addProduct(@Valid @RequestBody ProductDto productInfo) {
        try {
            return productService.addProduct(productInfo.name(), productInfo.quantity(), productInfo.supplierId());
        } catch (SupplierNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productInfo) {
        try {
            return productService.updateProduct(id, productInfo);
        } catch (SupplierNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
        }
    }

    @ExceptionHandler
    public ErrorResponse handleInputValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)).property("fieldErrors", fieldErrors).build();
    }
}
