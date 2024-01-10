package cids.demo.productstockmanager.supplier;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public Supplier getSupplier(@PathVariable Long id) {
        return supplierService.getSupplier(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
    }

    @PutMapping
    public Supplier addSupplier(@RequestBody SupplierDto supplierInfo) {
        return supplierService.addSupplier(supplierInfo.name(), supplierInfo.legalType(), supplierInfo.registrationNumber());
    }

    @PutMapping("/{id}")
    public void updateSupplier(@PathVariable Long id, @RequestBody SupplierDto supplierInfo) {
        Supplier newSupplier = new Supplier(supplierInfo.name(), supplierInfo.legalType(), supplierInfo.registrationNumber());
        supplierService.updateSupplier(newSupplier);
    }
}
