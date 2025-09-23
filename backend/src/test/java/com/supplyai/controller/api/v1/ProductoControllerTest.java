package com.supplyai.controller.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supplyai.exception.ResourceNotFoundException;
import com.supplyai.dto.ProductoDTO;
import com.supplyai.dto.response.ApiResponse;
import com.supplyai.controller.ProductoController;
import com.supplyai.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getProductosBySucursal_ShouldReturnPagedProducts_WhenSucursalExists() throws Exception {
        // Given
        Long sucursalId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        ProductoDTO productoDTO = new ProductoDTO(); // Populate with test data
        productoDTO.setId(1L);
        productoDTO.setNombre("Test Product");
        List<ProductoDTO> productoList = Collections.singletonList(productoDTO);
        Page<ProductoDTO> pagedProductos = new PageImpl<>(productoList, pageable, 1);

        ApiResponse<Page<ProductoDTO>> expectedResponse = ApiResponse.success(pagedProductos, "Productos recuperados exitosamente");

        given(productoService.findAllBySucursalId(anyLong(), any(Pageable.class))).willReturn(pagedProductos);

        // When & Then
        mockMvc.perform(get("/api/v1/productos/sucursal/{sucursalId}", sucursalId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Productos recuperados exitosamente"))
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].nombre").value("Test Product"));
    }

    @Test
    void getProductosBySucursal_ShouldReturnNotFound_WhenSucursalDoesNotExist() throws Exception {
        // Given
        Long sucursalId = 99L; // Non-existent ID
        String errorMessage = "Sucursal no encontrada con el ID: " + sucursalId;
        given(productoService.findAllBySucursalId(anyLong(), any(Pageable.class)))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When & Then
        mockMvc.perform(get("/api/v1/productos/sucursal/{sucursalId}", sucursalId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Sucursal no encontrada con el ID: 99"));
    }
}
