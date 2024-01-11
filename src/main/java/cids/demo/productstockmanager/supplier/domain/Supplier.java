package cids.demo.productstockmanager.supplier.domain;

import jakarta.persistence.*;

@Entity
public class Supplier {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private LegalType legalType;

    // CPF when the legal type is a natural person and CNPJ when it's a legal entity.
    private String registrationNumber;

    public Supplier(String name, LegalType legalType, String registrationNumber) {
        this.name = name;
        this.legalType = legalType;
        this.registrationNumber = registrationNumber;
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
}
