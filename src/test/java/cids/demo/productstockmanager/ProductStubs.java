package cids.demo.productstockmanager;

import cids.demo.productstockmanager.product.domain.Product;

import java.util.List;

public class ProductStubs {
    public static Product withNaturalPersonAsSupplier(Long id, Long supplierId) {
        Product product = new Product("test", 10, SupplierStubs.withNaturalPersonAsType(supplierId));
        product.setId(id);
        return product;
    }

    public static Product withLegalEntityAsSupplier(Long id, Long supplierId) {
        Product product = new Product("test", 10, SupplierStubs.withNaturalPersonAsType(supplierId));
        product.setId(id);
        return product;
    }

    public static Product withExceedingMaxQuantity(Long id, Long supplierId) {
        Product product = new Product("test", 10000, SupplierStubs.withLegalEntityAsType(supplierId));
        product.setId(id);
        return product;
    }

    public static Product withNegativeQuantity(Long id, Long supplierId) {
        Product product = new Product("test", -1, SupplierStubs.withLegalEntityAsType(supplierId));
        product.setId(id);
        return product;
    }

    public static Product withNullName(Long id, Long supplierId) {
        Product product = new Product(null, 0, SupplierStubs.withLegalEntityAsType(supplierId));
        product.setId(id);
        return product;
    }

    public static List<Product> twoProducts(Long id1, Long supplierId1, Long id2, Long supplierId2) {
        return List.of(withNaturalPersonAsSupplier(id1, supplierId1), withLegalEntityAsSupplier(id2, supplierId2));
    }
}
