package com.perfulandia.inventario.ControllerTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.inventario.assemblers.ResenaModelAssembler;
import com.perfulandia.inventario.controller.ResenaController;
import com.perfulandia.inventario.model.Resena;
import com.perfulandia.inventario.service.ResenaService;

@WebMvcTest(controllers = ResenaController.class)
@Import(ResenaControllerTest.MockConfig.class)
public class ResenaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResenaService resenaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ResenaService resenaService() {
            return Mockito.mock(ResenaService.class);
        }
        @Bean
        public ResenaModelAssembler resenaModelAssembler() {
            return new ResenaModelAssembler();
        }
    }


    @Test
    void testGuardarResena() throws Exception {
        Resena resena = new Resena();
        resena.setId(1L);
        resena.setComentario("Muy bueno");
        resena.setCalificacion(5);

        Mockito.when(resenaService.guardar(any(Resena.class))).thenReturn(resena);

        mockMvc.perform(post("/api/v2/resenas/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resena)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comentario").value("Muy bueno"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testListarResenas() throws Exception {
        Resena r1 = new Resena();
        Resena r2 = new Resena();
        Mockito.when(resenaService.listar()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v2/resenas/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.resenaModelList.length()").value(2));

    }

    @Test
    void testObtenerResenaPorId() throws Exception {
        Resena resena = new Resena();
        resena.setComentario("Excelente");
        Mockito.when(resenaService.buscarPorId(1L)).thenReturn(Optional.of(resena));

        mockMvc.perform(get("/api/v2/resenas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Excelente"));
    }

    @Test
    void testEliminarResenaExistente() throws Exception {
        Mockito.when(resenaService.buscarPorId(1L)).thenReturn(Optional.of(new Resena()));
        Mockito.doNothing().when(resenaService).eliminar(1L);

        mockMvc.perform(delete("/api/v2/resenas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerPromedioCalificacion() throws Exception {
        Mockito.when(resenaService.obtenerPromedioCalificacion(10L)).thenReturn(4.2);

        mockMvc.perform(get("/api/v2/resenas/producto/promedio/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.2"));
    }

    @Test
    void testReporteCantidadResenasPorProducto() throws Exception {
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("idProducto", 1L);
        reporte.put("cantidadResenas", 3L);

        Mockito.when(resenaService.obtenerCantidadResenasPorProducto()).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/v2/resenas/reporte/cantidad-por-producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidadResenas").value(3));
    }
    
    @Test
    void testActualizarResena() throws Exception {
        Long id = 1L;

        Resena resenaActualizada = new Resena();
        resenaActualizada.setId(id);
        resenaActualizada.setComentario("Actualizado");
        resenaActualizada.setCalificacion(4);

        // Simula que la reseña ya existe
        Mockito.when(resenaService.buscarPorId(id)).thenReturn(Optional.of(new Resena()));
        Mockito.when(resenaService.actualizar(any(Resena.class))).thenReturn(resenaActualizada);

        mockMvc.perform(put("/api/v2/resenas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Actualizado"));
    }

    @Test
    void testListarPorUsuario() throws Exception {
        Resena r1 = new Resena();
        Resena r2 = new Resena();
        Mockito.when(resenaService.listarPorUsuarioId(1L)).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v2/resenas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testActualizarResena_NoEncontrada() throws Exception {
        Long id = 999L; // ID que no existe

        Resena resenaActualizada = new Resena();
        resenaActualizada.setComentario("Comentario nuevo");
        resenaActualizada.setCalificacion(5);
        resenaActualizada.setId(id);

        // Simulamos que no se encuentra la reseña
        Mockito.when(resenaService.buscarPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v2/resenas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaActualizada)))
                .andExpect(status().isNotFound()); // 404
    }

}