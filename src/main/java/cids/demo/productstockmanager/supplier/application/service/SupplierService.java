package cids.demo.productstockmanager.supplier.application.service;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.supplier.application.port.in.SupplierDto;
import cids.demo.productstockmanager.supplier.application.port.out.SupplierRepository;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierRepository repository;

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

    public void updateSupplier(Long id, SupplierDto supplierInfo) throws SupplierNotFoundException {
        if (getSupplier(id).isEmpty()) {
            String err = String.format("Cannot update supplier with ID '%d' because no supplier with this ID was found.", id);
            LOGGER.error(err);
            throw new SupplierNotFoundException(err);
        }
        var updatedSupplier = new Supplier(supplierInfo.name(), supplierInfo.legalType(), supplierInfo.registrationNumber());
        updatedSupplier.setId(id);
        repository.save(updatedSupplier);
    }

    public void deleteSupplier(Long id) {
        repository.deleteById(id);
        LOGGER.info("Deleted supplier with ID '{}'", id);
    }
}
