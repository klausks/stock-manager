package cids.demo.productstockmanager.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

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

//    public Product addProduct(String name, int quantity, Long supplierId) {
//        Supplier su
//        Product toBeAdded = new Product(name, quantity, supplierId);
//        productRepository.add(new Product(name, quantity, supplierId));
//        return toBeAdded;
//    }



    public void updateProduct(Product product) {
        productRepository.updateById(product.getId(), product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
