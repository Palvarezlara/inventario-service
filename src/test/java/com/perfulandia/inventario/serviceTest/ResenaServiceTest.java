package com.perfulandia.inventario.serviceTest;

import com.perfulandia.inventario.model.Resena;
import com.perfulandia.inventario.repository.ResenaRepository;
import com.perfulandia.inventario.service.ResenaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ResenaServiceTest {

    @Mock
    private ResenaRepository repository;

    @InjectMocks
    private ResenaService service;

    private Resena resenaEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        resenaEjemplo = new Resena();
        resenaEjemplo.setId(1L);
        resenaEjemplo.setComentario("Muy bueno");
        resenaEjemplo.setCalificacion(5);
        resenaEjemplo.setIdProducto(10L);
        resenaEjemplo.setIdUsuario(100L);
        resenaEjemplo.setNombreUsuario("Juan");
    }

    @Test
    void testGuardar() {
        when(repository.save(resenaEjemplo)).thenReturn(resenaEjemplo);

        Resena resultado = service.guardar(resenaEjemplo);

        assertEquals(resenaEjemplo, resultado);
        verify(repository).save(resenaEjemplo);
    }

    @Test
    void testListar() {
        when(repository.findAll()).thenReturn(List.of(resenaEjemplo, resenaEjemplo));

        List<Resena> resultado = service.listar();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void testListarPorProductoId() {
        when(repository.findByIdProducto(10L)).thenReturn(List.of(resenaEjemplo));

        List<Resena> resultado = service.listarPorProductoId(10L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdProducto(10L);
    }

    @Test
    void testListarPorUsuarioId() {
        when(repository.findByIdUsuario(100L)).thenReturn(List.of(resenaEjemplo));

        List<Resena> resultado = service.listarPorUsuarioId(100L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdUsuario(100L);
    }

    @Test
    void testBuscarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(resenaEjemplo));

        Optional<Resena> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(resenaEjemplo, resultado.get());
    }

    @Test
    void testEliminar() {
        doNothing().when(repository).deleteById(1L);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testActualizar() {
        when(repository.save(resenaEjemplo)).thenReturn(resenaEjemplo);

        Resena resultado = service.actualizar(resenaEjemplo);

        assertEquals(resenaEjemplo, resultado);
    }

    @Test
    void testObtenerPromedioCalificacion() {
        when(repository.promedioCalificacionPorProducto(10L)).thenReturn(4.5);

        Double promedio = service.obtenerPromedioCalificacion(10L);

        assertEquals(4.5, promedio);
    }

    @Test
    void testObtenerCantidadResenasPorProducto() {
        Object[] fila1 = {10L, 3L};
        Object[] fila2 = {11L, 5L};

        when(repository.cantidadResenasPorProducto()).thenReturn(List.of(fila1, fila2));

        List<Map<String, Object>> resultado = service.obtenerCantidadResenasPorProducto();

        assertEquals(2, resultado.size());
        assertEquals(3L, resultado.get(0).get("cantidadResenas"));
        assertEquals(10L, resultado.get(0).get("idProducto"));
        assertEquals(5L, resultado.get(1).get("cantidadResenas"));
        assertEquals(11L, resultado.get(1).get("idProducto"));
    }
}
