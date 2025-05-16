package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*Controller se maneja el CRUD, esta con respuestas http */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

  @Autowired
  private ProductoService productoService;
//----------------------------EndPoint CRUD----------------------------//
  @GetMapping
  public ResponseEntity<List<Producto>> listarProductos() {
    List<Producto> productos = productoService.listar();
    return ResponseEntity.ok(productos);
}
  @PutMapping("/{id}")
public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productoActualizado) {
    Producto existente = productoService.buscarPorId(id);
    if (existente != null) {
        existente.setNombre(productoActualizado.getNombre());
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setStock(productoActualizado.getStock());
        Producto actualizado = productoService.guardar(existente);
        return ResponseEntity.ok(actualizado);
    } else {
        return ResponseEntity.notFound().build();
    }
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

//------------------EndPoint de la Logica de negocio------------------------//

/*EndPoint  para descontar del inventario */
  @PatchMapping("/rebajarStock/{id}")
  public ResponseEntity<?> rebajarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
    try {
        int cantidad = body.get("cantidad");
        Producto actualizado = productoService.rebajarStock(id, cantidad);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Stock actualizado correctamente",
            "producto", actualizado
        ));
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
 
/*EndPoint para reponer unidades al inventario */
  @PatchMapping("/{id}/reponer")
  public ResponseEntity<String> reponerStock(
        @PathVariable Long id,
        @RequestParam int cantidad) {

    try {
        productoService.reponerStock(id, cantidad);
        return ResponseEntity.ok("Stock repuesto correctamente.");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

/* EndPoint para buscar por nombre de producto*/
  @GetMapping("/buscar")
  public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
    List<Producto> productos = productoService.buscarPorNombre(nombre);
    if (productos.isEmpty()) {
        return ResponseEntity.notFound().build();
    } else {
        return ResponseEntity.ok(productos);
    }
}
  /*Endpoint para stock bajo */
  @GetMapping("/stock/bajo/{cantidad}")
  public ResponseEntity<List<Producto>> obtenerProductosConStockBajo(@PathVariable int cantidad) {
    List<Producto> productos = productoService.listarStockBajo(cantidad);
    return ResponseEntity.ok(productos);
}
  /*EndPoint para mostrar productos cuyo precio sea menor a cierta cantidad */
  @GetMapping("/precio/menor/{precio}")
  public ResponseEntity<List<Producto>> obtenerProductosPorPrecioMenor(@PathVariable double precio) {
    List<Producto> productos = productoService.listarPorPrecioMenorA(precio);
    return ResponseEntity.ok(productos);
}


}
