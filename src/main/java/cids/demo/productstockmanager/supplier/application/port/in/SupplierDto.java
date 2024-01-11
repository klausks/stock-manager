package cids.demo.productstockmanager.supplier.application.port.in;

import cids.demo.productstockmanager.supplier.domain.Supplier;
import jakarta.validation.constraints.NotNull;

public record SupplierDto(@NotNull String name, @NotNull Supplier.LegalType legalType, @NotNull String registrationNumber) {

}
