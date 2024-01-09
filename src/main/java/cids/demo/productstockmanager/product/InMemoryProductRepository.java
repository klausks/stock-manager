package cids.demo.productstockmanager.product;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryProductRepository implements ProductRepository {
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
    public void add(Product product) {
        products.putIfAbsent(product.getId(), product);
    }

    @Override
    public void updateById(Long id, Product product) {
        products.replace(id, product);
        products.get(id);
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }
}
