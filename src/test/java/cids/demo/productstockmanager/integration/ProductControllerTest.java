package cids.demo.productstockmanager.integration;

import cids.demo.productstockmanager.ProductStubs;
import cids.demo.productstockmanager.product.adapter.in.web.ProductController;
import cids.demo.productstockmanager.product.application.port.in.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        var products = ProductStubs.twoProducts();
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
        var product = ProductStubs.withLegalEntityAsSupplier();
        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.of(product));

        var expectedJsonResponseString = objectMapper.writeValueAsString(product);
        mockMvc.perform(get("/products/1")).andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedJsonResponseString));
    }

    @Test
    void givenProductExists_whenCallUpdateProduct_shouldReturnOkStatusAndUpdatedProductInfo() throws Exception {
        var existingProduct = ProductStubs.withLegalEntityAsSupplier();
        var requestBody = objectMapper.writeValueAsString(existingProduct);

        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.of(existingProduct));
        when(updateProductUseCase.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(existingProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(existingProduct)));
    }

    @Test
    void givenProductDoesNotExist_whenCallUpdateProduct_shouldReturnNotFoundStatus() throws Exception {
        var existingProduct = ProductStubs.withLegalEntityAsSupplier();
        var requestBody = objectMapper.writeValueAsString(existingProduct);

        when(getProductsUseCase.getProduct(anyLong())).thenReturn(Optional.of(existingProduct));
        when(updateProductUseCase.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(existingProduct);

        mockMvc.perform(put("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(existingProduct)));
    }
}
