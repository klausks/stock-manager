package cids.demo.productstockmanager.supplier;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemorySupplierRepository implements SupplierRepository {
    private final Map<Long, Supplier> suppliers = new HashMap<>();

    @Override
    public List<Supplier> findAll() {
        return List.copyOf(suppliers.values());
    }

    @Override
    public Supplier findById(Long id) {
        return suppliers.get(id);
    }

    @Override
    public void add(Supplier supplier) {
        suppliers.putIfAbsent(supplier.getId(), supplier);
    }

    @Override
    public void updateById(Long id, Supplier supplier) {
        suppliers.replace(id, supplier);
        suppliers.get(id);
    }

    @Override
    public void deleteById(Long id) {
        suppliers.remove(id);
    }
}
