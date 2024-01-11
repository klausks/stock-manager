package cids.demo.productstockmanager.product.domain;

import cids.demo.productstockmanager.supplier.domain.Supplier;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    private int quantity;

    @OneToOne
    @JoinColumn
    private Supplier supplier;

    public Product(String name, int quantity, Supplier supplier) throws IllegalArgumentException {
        if (quantity < 0 || quantity > 9999) {
            String err = String.format("Product quantity provided (%d) is not valid. Quantity must be between 0 and 9999", quantity);
            throw new IllegalArgumentException(err);
        }
        this.name = name;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    public Product() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
