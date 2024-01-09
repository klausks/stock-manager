package cids.demo.productstockmanager.product;

public record ProductDto(Long id, String name, int quantity, Long supplierId) {
}
