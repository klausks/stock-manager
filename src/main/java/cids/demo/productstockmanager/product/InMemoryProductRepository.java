package cids.demo.productstockmanager.product;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryProductRepository implements ProductRepository {
    private static final AtomicLong ID_COUNTER = new AtomicLong(1L);
    private final Map<Long, Product> products = new HashMap<>();

    @Override
    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    @Override
    public List<Product> findBySupplier(String supplierName) {
        List<Product> result = new ArrayList<>();
        products.forEach((id, product) -> {
            if (Objects.equals(product.getSupplier().getName(), supplierName)) {
                result.add(product);
            }
        });
        return result;
    }

    @Override
    public Product findById(Long id) {
        return products.get(id);
    }

    @Override
    public Product add(Product product) {
        product.setId(ID_COUNTER.getAndIncrement());
        products.putIfAbsent(product.getId(), product);
        return product;
    }

    @Override
    public void updateById(Long id, Product product) {
        products.replace(id, product);
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }
}
