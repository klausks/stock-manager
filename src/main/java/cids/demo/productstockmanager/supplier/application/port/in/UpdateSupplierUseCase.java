package cids.demo.productstockmanager.supplier.application.port.in;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;

public interface UpdateSupplierUseCase {
    void updateSupplier(Long id, SupplierDto supplierInfo) throws SupplierNotFoundException;
}
