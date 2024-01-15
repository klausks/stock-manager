package cids.demo.productstockmanager.unit.supplier;

import cids.demo.productstockmanager.SupplierStubs;
import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.supplier.application.port.in.SupplierDto;
import cids.demo.productstockmanager.supplier.application.port.out.SupplierRepository;
import cids.demo.productstockmanager.supplier.application.service.SupplierService;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class SupplierServiceTest {
    private final SupplierRepository mockSupplierRepository = Mockito.mock(SupplierRepository.class);
    private final SupplierService supplierService = new SupplierService(mockSupplierRepository);

    @Test
    void givenValidSupplierInfo_whenAddSupplier_shouldSaveAndReturn() {
        var supplier = SupplierStubs.withLegalEntityAsType(1L);
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

    @Test
    void givenValidSupplierInfo_whenUpdateSupplier_shouldUpdateAndReturn() throws SupplierNotFoundException {
        var existingSupplier = SupplierStubs.withLegalEntityAsType(1L);
        Mockito.when(mockSupplierRepository.findById(anyLong())).thenReturn(Optional.of(existingSupplier));

        var updatedSupplier = SupplierStubs.withNaturalPersonAsType(1L);
        updatedSupplier.setId(existingSupplier.getId());
        Mockito.when(mockSupplierRepository.save(any(Supplier.class))).thenReturn(updatedSupplier);
        var updateInfo = new SupplierDto(updatedSupplier.getName(), updatedSupplier.getLegalType(), updatedSupplier.getRegistrationNumber());

        var resultSupplier = supplierService.updateSupplier(existingSupplier.getId(), updateInfo);
        assertEquals(updatedSupplier, resultSupplier);
    }

    @Test
    void givenSupplierNotExists_whenUpdateSupplier_shouldThrowSupplierNotFoundException() {
        Mockito.when(mockSupplierRepository.findById(anyLong())).thenReturn(Optional.empty());
        var updateInfo = new SupplierDto("test", Supplier.LegalType.LEGAL_ENTITY, "123");
        assertThrows(SupplierNotFoundException.class, () -> supplierService.updateSupplier(1L, updateInfo), "Cannot update supplier with ID '1' because no supplier with this ID was found.");
    }


}
