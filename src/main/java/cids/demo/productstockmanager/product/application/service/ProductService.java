package cids.demo.productstockmanager.product.application.service;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.product.application.port.in.ProductDto;
import cids.demo.productstockmanager.product.application.port.out.ProductRepository;
import cids.demo.productstockmanager.product.domain.Product;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import cids.demo.productstockmanager.supplier.application.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

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
        Supplier supplier = supplierService.getSupplier(supplierId).get();
        if (supplier == null) {
            String errMsg = String.format("Cannot add product with supplerId '%d' because no supplier with this ID was found.", supplierId);
            throw new SupplierNotFoundException(errMsg);
        }
        return productRepository.save(new Product(name, quantity, supplier));
    }



    public void updateProduct(Long id, ProductDto productInfo) {
        Supplier supplier = supplierService.getSupplier(productInfo.supplierId()).get();
        Product updatedProduct = new Product(productInfo.name(), productInfo.quantity(), supplier);
        updatedProduct.setId(id);
        productRepository.save(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
