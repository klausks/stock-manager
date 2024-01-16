package cids.demo.productstockmanager;

import cids.demo.productstockmanager.supplier.application.port.in.SupplierDto;
import cids.demo.productstockmanager.supplier.domain.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierDtoStubs {
    public static SupplierDto withNaturalPersonAsType() {
        return new SupplierDto("Test", Supplier.LegalType.NATURAL_PERSON, "123");
    }

    public static SupplierDto withLegalEntityAsType() {
        return new SupplierDto("Test supplier", Supplier.LegalType.LEGAL_ENTITY, "123456");
    }

    public static SupplierDto withNullName() {
        return new SupplierDto(null, Supplier.LegalType.LEGAL_ENTITY, "123456");
    }

    public static SupplierDto withNullRegistrationNumber() {
        return new SupplierDto("Test", Supplier.LegalType.LEGAL_ENTITY, null);
    }

    public static SupplierDto withNullLegalType() {
        return new SupplierDto("Test", null, "123456");
    }

    public static List<SupplierDto> twoSuppliers() {
        return List.of(withLegalEntityAsType(), withNaturalPersonAsType());
    }
}
