package cids.demo.productstockmanager.product;

import cids.demo.productstockmanager.supplier.Supplier;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;

    @Column(length = 120)
    private String name;

    // TODO: Validation (max = 9999)
    private int quantity;

    @OneToOne
    @JoinColumn
    private Supplier supplier;

    public Product(String name, int quantity, Supplier supplier) {
        this.name = name;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    public Product() {

    }

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
