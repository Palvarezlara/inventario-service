package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;

import jakarta.validation.Valid;

/*Controller se maneja el CRUD, esta con respuestas http */
@RestController
@RequestMapping("/api/v2/productos")
public class ProductoController {

  @Autowired
  private ProductoService productoService;
//----------------------------EndPoint CRUD----------------------------//
  @GetMapping("/all")
  public ResponseEntity<List<Producto>> listarProductos() {
    List<Producto> productos = productoService.listar();
    return ResponseEntity.ok(productos); // 200 OK
}

  @PostMapping("/crear")
  public ResponseEntity<Producto> guardarProducto(@Valid @RequestBody Producto producto) {
    Producto guardado = productoService.guardar(producto);
    return ResponseEntity.status(201).body(guardado); // 201 Created
}

  @PutMapping("/{id}")
  public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productoActualizado) {
    Producto existente = productoService.buscarPorId(id);
    if (existente != null) {
        existente.setNombre(productoActualizado.getNombre());
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setStock(productoActualizado.getStock());
        Producto actualizado = productoService.guardar(existente);
        return ResponseEntity.ok(actualizado); // 200 OK
    } else {
        return ResponseEntity.notFound().build(); //404 Not Found
    }
}

  @GetMapping("/{id}")
  public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
    Producto producto = productoService.buscarPorId(id);
    if (producto != null) {
        return ResponseEntity.ok(producto); // 200 OK
    } else {
        return ResponseEntity.notFound().build();//404
    }
}
  
  @DeleteMapping("/eliminar/{id}")
  public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
    Producto producto = productoService.buscarPorId(id);
    if (producto != null) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();// 204 sin contenido
    } else {
        return ResponseEntity.notFound().build();//404
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
        )); // 200 OK
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // 400 Bad Request
    }
}
 
/*EndPoint para reponer unidades al inventario */
  @PatchMapping("/reponer/{id}")
  public ResponseEntity<String> reponerStock(
        @PathVariable Long id,
        @RequestParam int cantidad) {

    try {
        productoService.reponerStock(id, cantidad);
        return ResponseEntity.ok("Stock repuesto correctamente."); // 200 OK
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
    }
}

  /*EndPoint para buscar por nombre de producto*/
  @GetMapping("/buscar")
  public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
    List<Producto> productos = productoService.buscarPorNombre(nombre);
    if (productos.isEmpty()) {
        return ResponseEntity.notFound().build(); // 404 Not Found
    } else {
        return ResponseEntity.ok(productos); // 200 OK
    }
}
  
  /*Endpoint para stock bajo */
  @GetMapping("/stock/bajo/{cantidad}")
  public ResponseEntity<List<Producto>> obtenerProductosConStockBajo(@PathVariable int cantidad) {
    List<Producto> productos = productoService.listarStockBajo(cantidad);
    return ResponseEntity.ok(productos); // 200 OK
}
  
  /*EndPoint para mostrar productos cuyo precio sea menor a cierta cantidad */
  @GetMapping("/precio/menor/{precio}")
  public ResponseEntity<List<Producto>> obtenerProductosPorPrecioMenor(@PathVariable double precio) {
    List<Producto> productos = productoService.listarPorPrecioMenorA(precio);
    return ResponseEntity.ok(productos); // 200 OK
}
  
 /*EndPoint que devuelve los productos sin STOCK */
  @GetMapping("/sin-stock")
  public ResponseEntity<List<Producto>> obtenerProductosSinStock() {
    List<Producto> productosSinStock = productoService.listarStockBajo(1);
    return ResponseEntity.ok(productosSinStock); // 200 OK
  }
  
  /*EndPoint que devuelve los productos con stock */
  @GetMapping("/con-stock")
  public ResponseEntity<List<Producto>> obtenerProductosConStock() {
    List<Producto> productosConStock = productoService.listarConStock();
    return ResponseEntity.ok(productosConStock); // 200 OK
}

/*---------Endpoint Reportes----------- */

  /*Este endpoint devuelve el stock total de todos los productos en la base de datos. */
  @GetMapping("/stock/total")
  public ResponseEntity<Integer> obtenerStockTotal() {
    Integer total = productoService.obtenerStockTotal();
    return ResponseEntity.ok(total != null ? total : 0); // 200 OK
}

  /*Este endpoint devuelve un resumen del inventario, incluyendo el stock total y la cantidad de productos. */
  @GetMapping("/reporte/resumen")
  public ResponseEntity<Map<String, Object>> obtenerResumen() {
    return ResponseEntity.ok(productoService.obtenerResumenInventario()); // 200 OK
}

}
