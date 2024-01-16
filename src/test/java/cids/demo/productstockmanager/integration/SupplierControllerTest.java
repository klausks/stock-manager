package cids.demo.productstockmanager.integration;

import cids.demo.productstockmanager.ProductDtoStubs;
import cids.demo.productstockmanager.SupplierDtoStubs;
import cids.demo.productstockmanager.SupplierStubs;
import cids.demo.productstockmanager.product.application.SupplierNotFoundException;
import cids.demo.productstockmanager.supplier.adapter.in.web.SupplierController;
import cids.demo.productstockmanager.supplier.application.port.in.*;
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

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {
    @MockBean
    private AddSupplierUseCase addSupplierUseCase;

    @MockBean
    private GetSuppliersUseCase getSuppliersUseCase;

    @MockBean
    private DeleteSupplierUseCase deleteSupplierUseCase;

    @MockBean
    private UpdateSupplierUseCase updateSupplierUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Method not allowed tests
    @Test
    void whenCallPostWithIdInPath_shouldReturnMethodNotAllowed() throws Exception {
        var supplierJson = objectMapper.writeValueAsString(SupplierDtoStubs.withLegalEntityAsType());
        mockMvc.perform(post("/suppliers/1").content(supplierJson))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void whenCallPutWithoudIdInPath_shouldReturnMethodNotAllowed() throws Exception {
        var supplierJson = objectMapper.writeValueAsString(SupplierDtoStubs.withLegalEntityAsType());
        mockMvc.perform(put("/suppliers").content(supplierJson))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void whenCallDeleteWithoudIdInPath_shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(delete("/suppliers")).andExpect(status().isMethodNotAllowed());
    }

    // GET Tests
    @Test
    void givenNoSuppliers_whenCallGetSuppliers_returnOkStatusAndEmptyArray() throws Exception {
        mockMvc.perform(get("/suppliers"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json("[]"));
    }

    @Test
    void givenTwoSuppliers_whenCallGetSuppliers_returnOkStatusAndArrayWithTwoSuppliers() throws Exception {
        var suppliers = SupplierStubs.twoSuppliers(1L, 2L);
        when(getSuppliersUseCase.getAllSuppliers()).thenReturn(suppliers);
        String jsonWithSuppliers = objectMapper.writeValueAsString(suppliers);
        mockMvc.perform(get("/suppliers"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(jsonWithSuppliers));
    }

    @Test
    void givenNoSupplierExists_whenCallGetSupplier_returnNotFoundStatus() throws Exception {
        when(getSuppliersUseCase.getSupplier(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/suppliers/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void givenSupplierExists_whenCallGetSupplier_returnOkStatusAndSupplierInfo() throws Exception {
        var supplierId = 1L;
        var supplier = SupplierStubs.withLegalEntityAsType(1L);
        when(getSuppliersUseCase.getSupplier(anyLong())).thenReturn(Optional.of(supplier));
        var expectedJsonResponseString = objectMapper.writeValueAsString(supplier);
        mockMvc.perform(get("/suppliers/{supplierId}", supplierId)).andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedJsonResponseString));
    }

    // PUT (Update) tests
    @Test
    void givenSupplierExists_whenCallUpdateSupplier_shouldReturnOkStatusAndEmptyBody() throws Exception {
        var supplierId = 1L;
        var existingSupplier = SupplierStubs.withLegalEntityAsType(supplierId);
        var updatedSupplier = SupplierStubs.withNaturalPersonAsType(supplierId);
        updatedSupplier.setId(supplierId);

        when(getSuppliersUseCase.getSupplier(anyLong())).thenReturn(Optional.of(existingSupplier));
        when(updateSupplierUseCase.updateSupplier(anyLong(), any(SupplierDto.class))).thenReturn(updatedSupplier);

        var updateSupplierJson = objectMapper.writeValueAsString(SupplierDtoStubs.withNaturalPersonAsType());
        mockMvc.perform(put("/suppliers/{supplierId}", supplierId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateSupplierJson))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    @Test
    void givenSupplierDoesNotExist_whenCallUpdateSupplier_shouldReturnNotFoundStatus() throws Exception {
        var supplierId = 1L;
        var updateSupplierJson = objectMapper.writeValueAsString(ProductDtoStubs.withNaturalPersonAsSupplier(supplierId));

        when(getSuppliersUseCase.getSupplier(anyLong())).thenReturn(Optional.empty());
        when(updateSupplierUseCase.updateSupplier(anyLong(), any(SupplierDto.class))).thenThrow(SupplierNotFoundException.class);

        var productId = 1L;
        mockMvc.perform(put("/suppliers/{supplierId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateSupplierJson))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    // POST (Add) tests
    @Test
    void givenValidSupplierInfo_whenCallAddSupplier_shouldAddAndReturnEmptyBody() throws Exception {
        var supplierId = 1L;
        var addSupplierJson = objectMapper.writeValueAsString(SupplierDtoStubs.withLegalEntityAsType());
        var newSupplier = SupplierStubs.withLegalEntityAsType(supplierId);

        when(addSupplierUseCase.addSupplier(newSupplier.getName(), newSupplier.getLegalType(), newSupplier.getRegistrationNumber())).thenReturn(newSupplier);
        mockMvc.perform(post("/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addSupplierJson))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string(""));
    }

    @Test
    void givenInvalidSupplierInfo_whenCallAddSupplier_shouldReturnFieldErrors() throws Exception {
        var supplierId = 1L;
        var addSupplierWithNullNameJson = objectMapper.writeValueAsString(SupplierDtoStubs.withNullName());
        mockMvc.perform(post("/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addSupplierWithNullNameJson))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
               .andExpect(jsonPath("fieldErrors.name").hasJsonPath());
        var addSupplierWithNullRegistrationNumberJson = objectMapper.writeValueAsString(SupplierDtoStubs.withNullRegistrationNumber());
        mockMvc.perform(post("/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addSupplierWithNullRegistrationNumberJson))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
               .andExpect(jsonPath("fieldErrors.registrationNumber").hasJsonPath());
        var addSupplierWithNullLegalType = objectMapper.writeValueAsString(SupplierDtoStubs.withNullLegalType());
        mockMvc.perform(post("/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addSupplierWithNullLegalType))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
               .andExpect(jsonPath("fieldErrors.legalType").hasJsonPath());
    }
}
