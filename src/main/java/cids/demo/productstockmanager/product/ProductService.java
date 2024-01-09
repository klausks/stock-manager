package cids.demo.productstockmanager.product;

import cids.demo.productstockmanager.supplier.Supplier;
import cids.demo.productstockmanager.supplier.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
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

    public Product addProduct(String name, int quantity, Long supplierId) throws SupplierNotFoundException {
        Supplier supplier = supplierService.getSupplier(supplierId);
        if (supplier == null) {
            String errMsg = String.format("Cannot add product with supplerId '%d' because no supplier with this ID was found.", supplierId);
            throw new SupplierNotFoundException(errMsg);
        }
        return productRepository.add(new Product(name, quantity, supplier));
    }



    public void updateProduct(Long id, ProductDto productInfo) {
        Supplier supplier = supplierService.getSupplier(id);
        Product updatedProduct = new Product(productInfo.name(), productInfo.quantity(), supplier);
        updatedProduct.setId(id);
        productRepository.updateById(id, updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
