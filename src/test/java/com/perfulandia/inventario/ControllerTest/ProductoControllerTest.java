package com.perfulandia.inventario.ControllerTest;


import com.perfulandia.inventario.controller.ProductoController;
import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;
import com.perfulandia.inventario.assemblers.ProductoModelAssembler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.context.annotation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
@Import(ProductoControllerTest.MockConfig.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoService productoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ProductoService productoService() {
            return Mockito.mock(ProductoService.class);
        }

        @Bean
        public ProductoModelAssembler productoModelAssembler() {
            return new ProductoModelAssembler();
        }
    }

    @Test
    void testListarProductos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Perfume");
        producto.setStock(5);

        Mockito.when(productoService.listar()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v2/productos/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.productoModelList[0].nombre").value("Perfume"))
            .andExpect(jsonPath("$._embedded.productoModelList.length()").value(1))
            .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testListarProductosVacio() throws Exception {
        Mockito.when(productoService.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/productos/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded").doesNotExist());

    }


    @Test
    void testCrearProducto() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Perfume");
        producto.setStock(10);
        producto.setPrecio(5000.0);

        Mockito.when(productoService.guardar(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/v2/productos/crear")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre", is("Perfume")))
            .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testObtenerProductoPorId() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Perfume");

        Mockito.when(productoService.buscarPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/v2/productos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre", is("Perfume")));
    }

    @Test
    void testEliminarProducto() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);

        Mockito.when(productoService.buscarPorId(1L)).thenReturn(producto);
        Mockito.doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/api/v2/productos/eliminar/1"))
            .andExpect(status().isNoContent());
    }

   @Test
    void testRebajarStock() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(5);

        Mockito.when(productoService.rebajarStock(eq(1L), eq(2))).thenReturn(producto);

        Map<String, Integer> payload = Map.of("cantidad", 2);

        mockMvc.perform(patch("/api/v2/productos/rebajarStock/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje", is("Stock actualizado correctamente")));
    }


    @Test
    void testObtenerStockTotal() throws Exception {
        Mockito.when(productoService.obtenerStockTotal()).thenReturn(20);

        mockMvc.perform(get("/api/v2/productos/stock/total"))
            .andExpect(status().isOk())
            .andExpect(content().string("20"));
    }

    @Test
    void testObtenerStockTotalCero() throws Exception {
        Mockito.when(productoService.obtenerStockTotal()).thenReturn(0);

        mockMvc.perform(get("/api/v2/productos/stock/total"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }


    @Test
    void testObtenerResumenInventario() throws Exception {
        Mockito.when(productoService.obtenerResumenInventario())
               .thenReturn(Map.of("totalProductos", 10, "stockTotal", 50));

        mockMvc.perform(get("/api/v2/productos/reporte/resumen"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalProductos", is(10)))
            .andExpect(jsonPath("$.stockTotal", is(50)));
    }

    @Test
    void testObtenerResumenVacio() throws Exception {
        Mockito.when(productoService.obtenerResumenInventario()).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/v2/productos/reporte/resumen"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

}
