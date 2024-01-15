package cids.demo.productstockmanager.unit.product;

import cids.demo.productstockmanager.ProductStubs;
import cids.demo.productstockmanager.SupplierStubs;
import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.product.application.port.in.ProductDto;
import cids.demo.productstockmanager.product.application.port.out.ProductRepository;
import cids.demo.productstockmanager.product.application.service.ProductNotFoundException;
import cids.demo.productstockmanager.product.application.service.ProductService;
import cids.demo.productstockmanager.product.domain.Product;
import cids.demo.productstockmanager.supplier.application.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class ProductServiceTest {
    private final ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
    private final SupplierService mockSupplierService = Mockito.mock(SupplierService.class);
    private final ProductService productService = new ProductService(mockProductRepository, mockSupplierService);

    @Test
    void givenNoProducts_whenGetAllProducts_shouldReturnEmptyList() {
        Mockito.when(mockProductRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(productService.getAllProducts().isEmpty());
    }

    @Test
    void givenTwoProductsInRepository_whenGetAllProducts_shouldReturnListWithTwoProducts() {
        var products = ProductStubs.twoProducts(1L, 1L, 2L, 1L);
        Mockito.when(mockProductRepository.findAll()).thenReturn(products);
        List<Product> allProducts = productService.getAllProducts();
        assertEquals(2, allProducts.size());
        assertTrue(allProducts.containsAll(products));
    }

    @Test
    void givenProductWithIdExistsInRepository_whenGetProduct_shouldReturnProduct() {
        Product existingProduct = ProductStubs.withLegalEntityAsSupplier(1L, 1L);
        Mockito.when(mockProductRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Product getProductResult = productService.getProduct(1L).get();
        assertEquals(existingProduct, getProductResult);
    }

    @Test
    void givenValidProductInfo_whenAddProduct_shouldSaveAndReturn() {
        Product toBeAdded = ProductStubs.withLegalEntityAsSupplier(1L, 1L);
        Mockito.when(mockSupplierService.getSupplier(anyLong())).thenReturn(Optional.of(SupplierStubs.withLegalEntityAsType(1L)));
        Mockito.when(mockProductRepository.save(any(Product.class))).thenReturn(toBeAdded);
        try {
            Product added = productService.addProduct(toBeAdded.getName(), toBeAdded.getQuantity(), toBeAdded.getSupplier().getId());
            assertEquals(toBeAdded, added);
        } catch(SupplierNotFoundException ex) {
            fail();
        }
    }

    @Test
    void givenInvalidSupplierId_whenAddProduct_shouldThrowSupplierNotFoundException() {
        Product toBeAdded = ProductStubs.withLegalEntityAsSupplier(1L, 1L);
        Mockito.when(mockSupplierService.getSupplier(anyLong())).thenReturn(Optional.empty());
        Mockito.when(mockProductRepository.save(any(Product.class))).thenReturn(toBeAdded);
        assertThrows(SupplierNotFoundException.class, () -> productService.addProduct(toBeAdded.getName(), toBeAdded.getQuantity(), toBeAdded.getId()));
    }

    @Test
    void givenProductNotExists_whenUpdateProduct_throwProductNotFoundExcetion() {
        Mockito.when(mockProductRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.when(mockSupplierService.getSupplier(anyLong())).thenReturn(Optional.of(SupplierStubs.withLegalEntityAsType(1L)));
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, new ProductDto("test", 99, 1L)));
    }

    @Test
    void givenProductExists_whenUpdateProductWithInvalidSupplierId_throwSupplierNotFoundException() {
        Mockito.when(mockProductRepository.findById(anyLong())).thenReturn(Optional.of(ProductStubs.withLegalEntityAsSupplier(1L, 1L)));
        Mockito.when(mockSupplierService.getSupplier(anyLong())).thenReturn(Optional.empty());
        assertThrows(SupplierNotFoundException.class, () -> productService.updateProduct(1L, new ProductDto("test", 99, 1L)));
    }



}
