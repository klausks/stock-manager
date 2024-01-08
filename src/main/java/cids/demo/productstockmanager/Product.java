package cids.demo.productstockmanager;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;

    @Column(length = 120)
    private String name;

    // TODO: Validation (max = 9999)
    @Column
    private int quantity;

    @OneToOne
    @JoinColumn
    private Supplier supplier;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
