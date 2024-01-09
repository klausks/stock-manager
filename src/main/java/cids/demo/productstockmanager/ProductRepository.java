package cids.demo.productstockmanager;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findBySupplier(String supplierName);
    Product findById(Long id);
    void add(Product product);
    Product updateById(Long id, Product product);
    void deleteById(Long id);
}
