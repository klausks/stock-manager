package cids.demo.productstockmanager;

import cids.demo.productstockmanager.supplier.domain.Supplier;

import java.util.concurrent.atomic.AtomicLong;

public class SupplierStubs {
    private static AtomicLong ID_COUNTER = new AtomicLong(1L);

    public static void resetIdCounter() {
        ID_COUNTER.set(1L);
    }

    public static Supplier withNaturalPersonAsType() {
        var supplier = new Supplier("Test", Supplier.LegalType.NATURAL_PERSON, "123");
        supplier.setId(ID_COUNTER.getAndIncrement());
        return supplier;
    }

    public static Supplier withLegalEntityAsType() {
        var supplier = new Supplier("Test supplier", Supplier.LegalType.LEGAL_ENTITY, "123456");
        supplier.setId(ID_COUNTER.getAndIncrement());
        return supplier;
    }
}
