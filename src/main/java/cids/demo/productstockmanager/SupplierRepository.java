package cids.demo.productstockmanager;

import java.util.List;

public interface SupplierRepository {
    List<Supplier> findAll();
    Supplier findById(Long id);
    void add(Supplier supplier);
    void updateById(Long id, Supplier supplier);
    void deleteById(Long id);
}
