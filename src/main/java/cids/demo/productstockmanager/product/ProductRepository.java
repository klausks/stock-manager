package cids.demo.productstockmanager.product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findBySupplier(String supplierName);
    Product findById(Long id);
    Product add(Product product);
    void updateById(Long id, Product product);
    void deleteById(Long id);
}
