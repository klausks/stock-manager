package cids.demo.productstockmanager.supplier;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return supplierService.getSupplier(id).get();
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
    }

    @PutMapping
    public Supplier addSupplier(@Valid @RequestBody SupplierDto supplierInfo) {
        return supplierService.addSupplier(supplierInfo.name(), supplierInfo.legalType(), supplierInfo.registrationNumber());
    }

    @PutMapping("/{id}")
    public void updateSupplier(@Valid @PathVariable Long id, @RequestBody SupplierDto supplierInfo) {
        supplierService.updateSupplier(id, supplierInfo);
    }

    @ExceptionHandler
    public ErrorResponse handleInputValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)).property("fieldErrors", fieldErrors).build();
    }
}
