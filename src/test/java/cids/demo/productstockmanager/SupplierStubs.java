package cids.demo.productstockmanager;

import cids.demo.productstockmanager.supplier.domain.Supplier;

public class SupplierStubs {
    public static Supplier withNaturalPersonAsType() {
        return new Supplier("Test", Supplier.LegalType.NATURAL_PERSON, "123");
    }


    public static Supplier withLegalEntityAsType() {
        return new Supplier("Test supplier", Supplier.LegalType.LEGAL_ENTITY, "123456");
    }
}
