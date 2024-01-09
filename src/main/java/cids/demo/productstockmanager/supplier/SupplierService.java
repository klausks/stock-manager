package cids.demo.productstockmanager.supplier;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public Supplier getSupplier(Long id) {
        return repository.findById(id);
    }

    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }

    public Supplier addSupplier(String name, Supplier.LegalType legalType, String registrationNumber) {
        Supplier toBeAdded = new Supplier(name, legalType, registrationNumber);
        repository.add(toBeAdded);
        return toBeAdded;
    }

    public void updateSupplier(Supplier supplier) {
        repository.updateById(supplier.getId(), supplier);
    }

    public void deleteSupplier(Long id) {
        repository.deleteById(id);
    }
}
