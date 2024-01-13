package cids.demo.productstockmanager.supplier.domain;

import cids.demo.productstockmanager.product.domain.Product;
import jakarta.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class Supplier {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL) @Column(nullable = false)
    private LegalType legalType;

    @Column(nullable = false)
    // CPF when the legal type is a natural person and CNPJ when it's a legal entity.
    private String registrationNumber;

    public Supplier(String name, LegalType legalType, String registrationNumber) {
        validate(name, legalType, registrationNumber);
        this.name = name;
        this.legalType = legalType;
        this.registrationNumber = registrationNumber;
    }

    private static void validate(String name, LegalType legalType, String registrationNumber) {
        if (name == null || legalType == null || registrationNumber == null) {
            var nullField = name == null ? " name"
                    : legalType == null ? "legalType"
                    : registrationNumber == null ? "registrationNumber" : "";
            var err = "Supplier " + nullField + " cannot be null.";
            throw new IllegalArgumentException(err);
        }
    }

    public Supplier() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getName() {
        return name;
    }

    public LegalType getLegalType() {
        return legalType;
    }

    // DO NOT CHANGE THE ORDER OF THE ENUM VALUES
    public enum LegalType {
        NATURAL_PERSON, LEGAL_ENTITY
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Supplier)) {
            return false;
        }
        return ((Supplier) o).getId().equals(this.getId())
                && ((Supplier) o).getName().equals(this.getName())
                && ((Supplier) o).getLegalType().equals(this.getLegalType())
                && ((Supplier) o).getRegistrationNumber().equals(this.getRegistrationNumber());
    }

    @Override
    public String toString() {
        return String.format("Supplier [name=%s, legalType=%s, registrationNumber=%s]", name, legalType, registrationNumber);
    }
}
