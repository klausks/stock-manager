package cids.demo.productstockmanager.supplier.application.port.in;

import cids.demo.productstockmanager.supplier.domain.Supplier;

public interface AddSupplierUseCase {
    Supplier addSupplier(String name, Supplier.LegalType legalType, String registrationNumber);
}
