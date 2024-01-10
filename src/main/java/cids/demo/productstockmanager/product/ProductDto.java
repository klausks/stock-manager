package cids.demo.productstockmanager.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
        @NotNull String name,
        @Max(9999) int quantity,
        Long supplierId) {
}
