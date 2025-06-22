package com.perfulandia.inventario.serviceTest;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) //Activacion de Mockito en Junit 5
class ProductoServiceTest {


    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService service;

    @Test
    void testGuardarProducto() {
        Producto producto = new Producto();
        producto.setNombre("Perfume");
        producto.setStock(10);

        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = service.guardar(producto);

        assertEquals("Perfume", resultado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testListarProductos() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> lista = service.listar();

        assertEquals(2, lista.size());
        verify(productoRepository).findAll();
    }

    @Test
    void testListarStockBajo() {
        Producto p1 = new Producto();
        p1.setStock(2);
        Producto p2 = new Producto();
        p2.setStock(3);

        when(productoRepository.findByStockLessThan(5)).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = service.listarStockBajo(5);

        assertEquals(2, resultado.size());
        verify(productoRepository).findByStockLessThan(5);
    }

    @Test
    void testListarPorPrecioMenorA() {
        Producto p1 = new Producto();
        p1.setPrecio(5000);
        Producto p2 = new Producto();
        p2.setPrecio(7000);

        when(productoRepository.findByPrecioLessThan(10000)).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = service.listarPorPrecioMenorA(10000);

        assertEquals(2, resultado.size());
        verify(productoRepository).findByPrecioLessThan(10000);
    }

    @Test
    void testListarSinStock() {
        Producto p1 = new Producto();
        p1.setStock(0);

        when(productoRepository.findByStockLessThan(1)).thenReturn(Arrays.asList(p1));

        List<Producto> resultado = service.listarSinStock();

        assertEquals(1, resultado.size());
        verify(productoRepository).findByStockLessThan(1);
    }


    @Test
    void testEliminarProducto() {
        Long id = 1L;
        doNothing().when(productoRepository).deleteById(id);

        service.eliminar(id);

        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void testRebajarStockCorrectamente() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(10);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto actualizado = service.rebajarStock(1L, 5);

        assertEquals(5, actualizado.getStock());
    }

    @Test
    void testRebajarStockInsuficiente() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(3);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.rebajarStock(1L, 10);
        });

        assertEquals("Stock insuficiente para el producto con ID: 1", ex.getMessage());
    }

    @Test
    void testReponerStock() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(2);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        service.reponerStock(1L, 5);

        assertEquals(7, producto.getStock());
    }

    @Test
    void testObtenerResumenInventario() {
        Producto p1 = new Producto();
        p1.setStock(2);
        p1.setPrecio(5000);

        Producto p2 = new Producto();
        p2.setStock(3);
        p2.setPrecio(10000);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        var resumen = service.obtenerResumenInventario();

        assertEquals(2, ((List<?>) resumen.get("productos")).size());
        assertEquals(5, resumen.get("totalStock"));
        assertEquals(2*5000 + 3*10000, (double) resumen.get("valorInventario"));
    }

    @Test
    void testBuscarPorNombre() {
        Producto p1 = new Producto();
        p1.setNombre("Perfume Rosado");

        when(productoRepository.findByNombreContainingIgnoreCase("rosado")).thenReturn(Arrays.asList(p1));

        List<Producto> resultado = service.buscarPorNombre("rosado");

        assertEquals(1, resultado.size());
        verify(productoRepository).findByNombreContainingIgnoreCase("rosado");
    }
     
    @Test
    void testBuscarPorNombreNoEncontrado() {
        when(productoRepository.findByNombreContainingIgnoreCase("noexiste")).thenReturn(Arrays.asList());

        List<Producto> resultado = service.buscarPorNombre("noexiste");

        assertEquals(0, resultado.size());
        verify(productoRepository).findByNombreContainingIgnoreCase("noexiste");
    }

}

