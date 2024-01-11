package cids.demo.productstockmanager.product.application.service;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.product.application.port.in.ProductDto;
import cids.demo.productstockmanager.product.application.port.out.ProductRepository;
import cids.demo.productstockmanager.product.domain.Product;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import cids.demo.productstockmanager.supplier.application.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplierId(supplierId);
    }

    public Product addProduct(String name, int quantity, Long supplierId) throws SupplierNotFoundException {
        Supplier supplier = supplierService.getSupplier(supplierId).orElseThrow(() -> {
            String err = String.format("Cannot add product with supplerId '%d' because no supplier with this ID was found.", supplierId);
            LOGGER.error(err);
            return new SupplierNotFoundException(err);
        });
        return productRepository.save(new Product(name, quantity, supplier));
    }

    public Product updateProduct(Long id, ProductDto productInfo) throws SupplierNotFoundException {
        Supplier supplier = supplierService.getSupplier(productInfo.supplierId()).orElseThrow(() -> {
            String err = String.format("Cannot update product with supplerId '%d' because no supplier with this ID was found.", productInfo.supplierId());
            LOGGER.error(err);
            return new SupplierNotFoundException(err);
        });
        Product updatedProduct = new Product(productInfo.name(), productInfo.quantity(), supplier);
        updatedProduct.setId(id);
        return productRepository.save(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
