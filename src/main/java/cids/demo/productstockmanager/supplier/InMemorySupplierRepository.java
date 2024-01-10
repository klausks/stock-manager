package cids.demo.productstockmanager.supplier;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemorySupplierRepository implements SupplierRepository {
    private static AtomicLong ID_COUNTER = new AtomicLong(1L);
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
        supplier.setId(ID_COUNTER.getAndIncrement());
        suppliers.putIfAbsent(supplier.getId(), supplier);
    }

    @Override
    public void updateById(Long id, Supplier supplier) {
        suppliers.replace(id, supplier);
    }

    @Override
    public void deleteById(Long id) {
        suppliers.remove(id);
    }
}
