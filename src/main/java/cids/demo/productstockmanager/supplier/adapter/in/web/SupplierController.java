package cids.demo.productstockmanager.supplier.adapter.in.web;

import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.supplier.application.port.in.SupplierDto;
import cids.demo.productstockmanager.supplier.application.service.SupplierService;
import cids.demo.productstockmanager.supplier.domain.Supplier;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierController.class);
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
        return supplierService.getSupplier(id).orElseThrow(() -> {
            String err = String.format("Supplier with ID '%d' not found.", id);
            LOGGER.error(err);
            return new ResourceNotFoundException(err);
        });
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
        try {
            supplierService.updateSupplier(id, supplierInfo);
        } catch (SupplierNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }

    }

    @ExceptionHandler
    public ErrorResponse handleInputValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ErrorResponse.builder(ex, ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)).property("fieldErrors", fieldErrors).build();
    }
}
