package cids.demo.productstockmanager.unit.product.domain;

import cids.demo.productstockmanager.ProductStubs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    public void givenInvalidParameters_whenConstructProduct_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ProductStubs.withExceedingMaxQuantity(1L, 1L));
        assertThrows(IllegalArgumentException.class, () -> ProductStubs.withNegativeQuantity(1L, 1L));
        assertThrows(IllegalArgumentException.class, () -> ProductStubs.withNullName(1L, 1L));
    }
}
