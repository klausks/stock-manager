package cids.demo.productstockmanager.supplier.adapter.in.web;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.supplier.application.port.in.*;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierController.class);
    private final GetSuppliersUseCase getSuppliersUseCase;
    private final AddSupplierUseCase addSupplierUseCase;
    private final DeleteSupplierUseCase deleteSupplierUseCase;
    private final UpdateSupplierUseCase updateSupplierUseCase;

    public SupplierController(GetSuppliersUseCase getSuppliersUseCase,
                              AddSupplierUseCase addSupplierUseCase,
                              DeleteSupplierUseCase deleteSupplierUseCase,
                              UpdateSupplierUseCase updateSupplierUseCase) {
        this.getSuppliersUseCase = getSuppliersUseCase;
        this.addSupplierUseCase = addSupplierUseCase;
        this.deleteSupplierUseCase = deleteSupplierUseCase;
        this.updateSupplierUseCase = updateSupplierUseCase;
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return getSuppliersUseCase.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public Supplier getSupplier(@PathVariable Long id) {
        return getSuppliersUseCase.getSupplier(id).orElseThrow(() -> {
            String err = String.format("Supplier with ID '%d' not found.", id);
            LOGGER.error(err);
            return new ResourceNotFoundException(err);
        });
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        deleteSupplierUseCase.deleteSupplier(id);
    }

    @PostMapping
    public ResponseEntity<Supplier> addSupplier(@Valid @RequestBody SupplierDto supplierInfo) {
        Long newProductId = addSupplierUseCase.addSupplier(supplierInfo.name(), supplierInfo.legalType(), supplierInfo.registrationNumber()).getId();
        var location = "/suppliers/" + newProductId;
        return ResponseEntity.created(URI.create(location)).build();
    }

    @PutMapping("/{id}")
    public void updateSupplier(@Valid @PathVariable Long id, @RequestBody SupplierDto supplierInfo) {
        try {
            updateSupplierUseCase.updateSupplier(id, supplierInfo);
        } catch (SupplierNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }

    }

    @ExceptionHandler
    public ErrorResponse handleInputValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)).property("fieldErrors", fieldErrors).build();
    }
}
