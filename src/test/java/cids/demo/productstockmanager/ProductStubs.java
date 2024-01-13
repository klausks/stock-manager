package cids.demo.productstockmanager;

import cids.demo.productstockmanager.product.domain.Product;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ProductStubs {
    private static AtomicLong ID_COUNTER = new AtomicLong(1L);

    public static void resetIdCounter() {
        ID_COUNTER.set(1L);
    }
    public static Product withNaturalPersonAsSupplier() {
        Product product = new Product("test", 10, SupplierStubs.withNaturalPersonAsType());
        product.setId(ID_COUNTER.getAndIncrement());
        return product;
    }

    public static Product withLegalEntityAsSupplier() {
        Product product = new Product("test", 10, SupplierStubs.withNaturalPersonAsType());
        product.setId(ID_COUNTER.getAndIncrement());
        return product;
    }

    public static Product withExceedingMaxQuantity() {
        Product product = new Product("test", 10000, SupplierStubs.withLegalEntityAsType());
        product.setId(ID_COUNTER.getAndIncrement());
        return product;
    }

    public static Product withNegativeQuantity() {
        Product product = new Product("test", -1, SupplierStubs.withLegalEntityAsType());
        product.setId(ID_COUNTER.getAndIncrement());
        return product;
    }

    public static Product withNullName() {
        Product product = new Product(null, 0, SupplierStubs.withLegalEntityAsType());
        product.setId(ID_COUNTER.getAndIncrement());
        return product;
    }

    public static List<Product> twoProducts() {
        return List.of(withNaturalPersonAsSupplier(), withLegalEntityAsSupplier());
    }
}
