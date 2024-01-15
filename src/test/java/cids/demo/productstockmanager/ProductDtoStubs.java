package cids.demo.productstockmanager;

import cids.demo.productstockmanager.product.application.port.in.ProductDto;

import java.util.List;

public class ProductDtoStubs {
    public static ProductDto withNaturalPersonAsSupplier(Long supplierId) {
        return new ProductDto("test", 10, supplierId);
    }

    public static ProductDto withLegalEntityAsSupplier(Long supplierId) {
        return new ProductDto("test", 10, supplierId);
    }

    public static ProductDto withExceedingMaxQuantity(Long supplierId) {
        return new ProductDto("test", 10000, supplierId);
    }

    public static ProductDto withNegativeQuantity(Long supplierId) {
        return new ProductDto("test", -1, supplierId);
    }

    public static ProductDto withNullName(Long supplierId) {
        return new ProductDto(null, 0, supplierId);
    }

    public static List<ProductDto> twoProducts(Long supplierId1, Long supplierId2) {
        return List.of(withNaturalPersonAsSupplier(supplierId1), withLegalEntityAsSupplier(supplierId2));
    }
}
