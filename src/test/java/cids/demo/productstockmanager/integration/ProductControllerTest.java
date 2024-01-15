package cids.demo.productstockmanager.integration;

import cids.demo.productstockmanager.ProductDtoStubs;
import cids.demo.productstockmanager.ProductStubs;
import cids.demo.productstockmanager.product.adapter.in.web.ProductController;
import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.product.application.port.in.*;
import cids.demo.productstockmanager.product.application.service.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    private AddProductUseCase addProductUseCase;

    @MockBean
    private GetProductsUseCase getProductsUseCase;

    @MockBean
    private UpdateProductUseCase updateProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Method not allowed tests
    @Test
    void whenCallPostWithIdInPath_shouldReturnMethodNotAllowed() throws Exception {
        var productJson = objectMapper.writeValueAsString(ProductDtoStubs.withLegalEntityAsSupplier(1L));
        mockMvc.perform(post("/products/1").content(productJson))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void whenCallPutWithoudIdInPath_shouldReturnMethodNotAllowed() throws Exception {
        var productJson = objectMapper.writeValueAsString(ProductDtoStubs.withLegalEntityAsSupplier(1L));
        mockMvc.perform(put("/products").content(productJson))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void whenCallDeleteWithoudIdInPath_shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(delete("/products")).andExpect(status().isMethodNotAllowed());
    }

    // GET Tests
    @Test
    void givenNoProducts_whenCallGetProducts_returnOkStatusAndEmptyArray() throws Exception {
        mockMvc.perform(get("/products"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json("[]"));
    }

    @Test
    void givenTwoProducts_whenCallGetProducts_returnOkStatusAndArrayWithTwoProducts() throws Exception {
        var products = ProductStubs.twoProducts(1L, 1L, 2L, 1L);
        when(getProductsUseCase.getAllProducts()).thenReturn(products);
        String expectedJsonString = objectMapper.writeValueAsString(products);
        mockMvc.perform(get("/products"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedJsonString));
    }

    @Test
    void givenNoProductExists_whenCallGetProduct_returnNotFoundStatus() throws Exception {
        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/products/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void givenProductExists_whenCallGetProduct_returnOkStatusAndProductInfo() throws Exception {
        var product = ProductStubs.withLegalEntityAsSupplier(1L, 1L);
        var productId = product.getId();
        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.of(product));
        var expectedJsonResponseString = objectMapper.writeValueAsString(product);
        mockMvc.perform(get("/products/{productId}", productId)).andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedJsonResponseString));
    }

    // Update tests
    @Test
    void givenProductExists_whenCallUpdateProduct_shouldReturnOkStatusAndUpdatedProductInfo() throws Exception {
        var existingProduct = ProductStubs.withLegalEntityAsSupplier(1L, 1L);
        var updatedProduct = ProductStubs.withNaturalPersonAsSupplier(1L, 2L);
        updatedProduct.setId(existingProduct.getId());

        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.of(existingProduct));
        when(updateProductUseCase.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(updatedProduct);

        var productId = existingProduct.getId();
        var updateProductJson = objectMapper.writeValueAsString(ProductDtoStubs.withNaturalPersonAsSupplier(2L));
        mockMvc.perform(put("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProductJson))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(updatedProduct)));
    }

    @Test
    void givenProductDoesNotExist_whenCallUpdateProduct_shouldReturnNotFoundStatus() throws Exception {
        var existingProduct = ProductStubs.withLegalEntityAsSupplier(1L, 1L);
        var updateProductJson = objectMapper.writeValueAsString(ProductDtoStubs.withNaturalPersonAsSupplier(1L));

        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.empty());
        when(updateProductUseCase.updateProduct(anyLong(), any(ProductDto.class))).thenThrow(ProductNotFoundException.class);

        var productId = existingProduct.getId();
        mockMvc.perform(put("/products/{productId}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateProductJson))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    // Add tests
    @Test
    void givenValidProductInfo_whenCallAddProduct_shouldAddAndReturnProductInfo() throws Exception {
        var supplierId = 1L;
        var productId = 1L;
        var addProductJson = objectMapper.writeValueAsString(ProductDtoStubs.withLegalEntityAsSupplier(supplierId));
        var newProduct = ProductStubs.withLegalEntityAsSupplier(productId, supplierId);

        when(addProductUseCase.addProduct(newProduct.getName(), newProduct.getQuantity(), supplierId)).thenReturn(newProduct);
        mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addProductJson))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(newProduct)));
    }

    @Test
    void givenInvalidProductInfo_whenCallAddProduct_shouldReturnFieldErrors() throws Exception {
        var supplierId = 1L;
        var addProductWithNullNameJson = objectMapper.writeValueAsString(ProductDtoStubs.withNullName(supplierId));
        mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addProductWithNullNameJson))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
               .andExpect(jsonPath("fieldErrors.name").hasJsonPath());
        var addProductWithExceedingMaxQtyJson = objectMapper.writeValueAsString(ProductDtoStubs.withExceedingMaxQuantity(supplierId));
        mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addProductWithExceedingMaxQtyJson))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
               .andExpect(jsonPath("fieldErrors.quantity").hasJsonPath());
        var addProductWithNegativeQtyJson = objectMapper.writeValueAsString(ProductDtoStubs.withNegativeQuantity(supplierId));
        mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addProductWithNegativeQtyJson))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
               .andExpect(jsonPath("fieldErrors.quantity").hasJsonPath());
    }

    @Test
    void givenNonExistentSupplierId_whenCallAddProduct_shouldReturnNotFound() throws Exception {
        var newProduct = ProductDtoStubs.withLegalEntityAsSupplier(1L);
        var requestBody = objectMapper.writeValueAsString(newProduct);

        when(addProductUseCase.addProduct(anyString(), anyInt(), anyLong())).thenThrow(SupplierNotFoundException.class);

        mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
               .andDo(print())
               .andExpect(status().isNotFound());
    }
}
