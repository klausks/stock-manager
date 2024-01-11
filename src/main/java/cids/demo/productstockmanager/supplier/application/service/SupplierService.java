package cids.demo.productstockmanager.supplier.application.service;

import cids.demo.productstockmanager.supplier.application.port.in.SupplierDto;
import cids.demo.productstockmanager.supplier.application.port.out.SupplierRepository;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public Optional<Supplier> getSupplier(Long id) {
        return repository.findById(id);
    }

    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }

    public Supplier addSupplier(String name, Supplier.LegalType legalType, String registrationNumber) {
        Supplier toBeAdded = new Supplier(name, legalType, registrationNumber);
        repository.save(toBeAdded);
        return toBeAdded;
    }

    public void updateSupplier(Long id, SupplierDto supplierInfo) {
        Supplier updatedSupplier = new Supplier(supplierInfo.name(), supplierInfo.legalType(), supplierInfo.registrationNumber());
        updatedSupplier.setId(id);
        repository.save(updatedSupplier);
    }

    public void deleteSupplier(Long id) {
        repository.deleteById(id);
    }
}
