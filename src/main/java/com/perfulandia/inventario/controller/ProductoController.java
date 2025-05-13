package com.perfulandia.inventario.controller;

import java.util.List;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/productos")
public class ProductoController {

  @Autowired
  private ProductoService productoService;

  @GetMapping
  public ResponseEntity<List<Producto>> listarProductos() {
    List<Producto> productos = productoService.listar();
    return ResponseEntity.ok(productos);
}
  
  @GetMapping("/{id}")
  public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
    Producto producto = productoService.buscarPorId(id);
    if (producto != null) {
        return ResponseEntity.ok(producto);
    } else {
        return ResponseEntity.notFound().build();
    }
}

  @PostMapping
  public ResponseEntity<Producto> guardarProducto(@Valid @RequestBody Producto producto) {
    Producto guardado = productoService.guardar(producto);
    return ResponseEntity.ok(guardado);
}
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
    Producto producto = productoService.buscarPorId(id);
    if (producto != null) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204 sin contenido
    } else {
        return ResponseEntity.notFound().build(); // 404
    }
}
}
