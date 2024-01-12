package cids.demo.productstockmanager.product.application.port.in;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.product.application.service.ProductNotFoundException;
import cids.demo.productstockmanager.product.domain.Product;

public interface UpdateProductUseCase {
    Product updateProduct(Long id, ProductDto productInfo) throws SupplierNotFoundException, ProductNotFoundException;
}
