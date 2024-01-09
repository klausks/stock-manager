package cids.demo.productstockmanager.product;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return productService.getProduct(id);
    }

    @GetMapping("/supplier/{name}")
    public List<Product> getProducts(@PathVariable String name) {
        return productService.getProductsBySupplier(name);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody ProductDto productInfo) throws SupplierNotFoundException {
        return productService.addProduct(productInfo.name(), productInfo.quantity(), productInfo.supplierId());
    }

    @PostMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductDto productInfo) {
        productService.updateProduct(id, productInfo);
    }

}
