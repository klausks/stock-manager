package cids.demo.productstockmanager.supplier.application.port.in;

import cids.demo.productstockmanager.supplier.domain.Supplier;

import java.util.List;
import java.util.Optional;

public interface GetSuppliersUseCase {
    Optional<Supplier> getSupplier(Long id);
    List<Supplier> getAllSuppliers();
}
