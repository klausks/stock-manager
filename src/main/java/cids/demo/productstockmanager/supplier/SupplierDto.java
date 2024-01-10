package cids.demo.productstockmanager.supplier;

import jakarta.validation.constraints.NotNull;

public record SupplierDto(
        @NotNull String name,
        @NotNull Supplier.LegalType legalType,
        @NotNull String registrationNumber) {

}
