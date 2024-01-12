package cids.demo.productstockmanager.product.application.port.in;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.product.domain.Product;

public interface AddProductUseCase {
    Product addProduct(String name, int quantity, Long supplierId) throws SupplierNotFoundException;
}
