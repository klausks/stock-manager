package cids.demo.productstockmanager.unit.supplier;

import cids.demo.productstockmanager.SupplierStubs;
import cids.demo.productstockmanager.supplier.application.port.out.SupplierRepository;
import cids.demo.productstockmanager.supplier.application.service.SupplierService;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class SupplierServiceTest {
    private final SupplierRepository mockSupplierRepository = Mockito.mock(SupplierRepository.class);
    private final SupplierService supplierService = new SupplierService(mockSupplierRepository);

    @BeforeEach
    void resetIdCounter() {
        SupplierStubs.resetIdCounter();
    }

    @Test
    void givenValidSupplierInfo_whenAddSupplier_shouldSaveAndReturn() {
        var supplier = SupplierStubs.withLegalEntityAsType();
        Mockito.when(mockSupplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        var addedSupplier = supplierService.addSupplier(supplier.getName(), supplier.getLegalType(), supplier.getRegistrationNumber());
        assertEquals(supplier, addedSupplier);
    }

    @Test
    void givenInvalidSupplierInfo_whenAddSupplier_shouldThrowIllegalArgumentException() {
        String supplierName = "test";
        String registrationNumber = "123";
        Supplier.LegalType legalType = Supplier.LegalType.LEGAL_ENTITY;
        assertThrows(IllegalArgumentException.class, () -> supplierService.addSupplier(null, legalType, registrationNumber));
        assertThrows(IllegalArgumentException.class, () -> supplierService.addSupplier(supplierName, null, registrationNumber));
        assertThrows(IllegalArgumentException.class, () -> supplierService.addSupplier(supplierName, legalType, null));
    }

}
