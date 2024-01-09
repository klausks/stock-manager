package cids.demo.productstockmanager;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //TODO: Can we use the supplier name or should we use ID?
    public List<Product> getProductsBySupplier(String supplierName) {
        return productRepository.findBySupplier(supplierName);
    }

    public Product addProduct(String name, int quantity, Supplier supplier) {
        Product toBeAdded = new Product(name, quantity, supplier);
        productRepository.add(new Product(name, quantity, supplier));
        return toBeAdded;
    }

    public void updateProduct(Product product) {
        productRepository.updateById(product.getId(), product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
