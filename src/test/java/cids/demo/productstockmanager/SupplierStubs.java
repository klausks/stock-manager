package cids.demo.productstockmanager;

import cids.demo.productstockmanager.supplier.domain.Supplier;

import java.util.List;

public class SupplierStubs {
    public static Supplier withNaturalPersonAsType(Long id) {
        var supplier = new Supplier("Test", Supplier.LegalType.NATURAL_PERSON, "123");
        supplier.setId(id);
        return supplier;
    }

    public static Supplier withLegalEntityAsType(Long id) {
        var supplier = new Supplier("Test supplier", Supplier.LegalType.LEGAL_ENTITY, "123456");
        supplier.setId(id);
        return supplier;
    }

    public static Supplier withNullName(Long id) {
        var supplier = new Supplier(null, Supplier.LegalType.LEGAL_ENTITY, "123456");
        supplier.setId(id);
        return supplier;
    }

    public static Supplier withNullRegistrationNumber(Long id) {
        var supplier = new Supplier(null, Supplier.LegalType.LEGAL_ENTITY, "123456");
        supplier.setId(id);
        return supplier;
    }

    public static List<Supplier> twoSuppliers(Long id1, Long id2) {
        return List.of(withLegalEntityAsType(id1), withNaturalPersonAsType(id2));
    }
}
