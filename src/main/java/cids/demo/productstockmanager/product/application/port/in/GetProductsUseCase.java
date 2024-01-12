package cids.demo.productstockmanager.product.application.port.in;

import cids.demo.productstockmanager.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface GetProductsUseCase {
    Optional<Product> getProduct(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsBySupplier(Long supplierId);
}
