package cids.demo.productstockmanager.supplier.application.port.in;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.supplier.domain.Supplier;

public interface UpdateSupplierUseCase {
    Supplier updateSupplier(Long id, SupplierDto supplierInfo) throws SupplierNotFoundException;
}
