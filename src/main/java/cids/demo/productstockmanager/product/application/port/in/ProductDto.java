package cids.demo.productstockmanager.product.application.port.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductDto(@NotNull String name, @Min(0) @Max(9999) int quantity, Long supplierId) {
}
