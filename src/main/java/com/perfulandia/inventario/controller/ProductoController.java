package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

import com.perfulandia.inventario.assemblers.ProductoModelAssembler;
import com.perfulandia.inventario.dto.ProductoModel;
import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


/*Controller se maneja el CRUD, esta con respuestas http */
@RestController
@RequestMapping("/api/v2/productos")
@Tag(name = "Producto", description = "Operaciones relacionadas con productos del inventario")
public class ProductoController {
  
  @Autowired
  private ProductoService productoService;

  @Autowired
  private ProductoModelAssembler assembler;

  
//----------------------------EndPoint CRUD----------------------------//
  @GetMapping("/all")
  @Operation(summary = "Listar todos los productos")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "400", description = "Error al obtener la lista de productos")
  })
  public ResponseEntity<CollectionModel<ProductoModel>> listarProductos() {
   try {
        List<Producto> productos = productoService.listar();
        List<ProductoModel> modelos = productos.stream()
            .map(assembler::toModel)
            .toList();
        return ResponseEntity.ok(CollectionModel.of(modelos, 
        linkTo(methodOn(ProductoController.class)
        .listarProductos()).withSelfRel()
        )
      ); // 200 OK
    } catch (Exception e) {
        return ResponseEntity.badRequest().build(); // 400 Bad Request
    }
}

  @PostMapping("/crear")
  @Operation(summary = "Crear un nuevo producto",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Objeto Producto a crear",
        required = true,
        content = @Content(schema = @Schema(implementation = Producto.class))
    ))  
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
      content = @Content(mediaType = "application/json", 
                          schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos inválidos")
  })
  public ResponseEntity<ProductoModel> guardarProducto(@Valid @RequestBody Producto producto) {
    Producto guardado = productoService.guardar(producto);
    ProductoModel model = assembler.toModel(guardado);

    return ResponseEntity
            .created(linkTo(methodOn(ProductoController.class).obtenerProductoPorId(guardado.getId())).toUri())
            .body(model); // Link al producto recién creado
}

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar un producto existente",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos actualizados del producto",
        required = true,
        content = @Content(schema = @Schema(implementation = Producto.class))
    ))
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
      content = @Content(mediaType = "application/json", 
                          schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
  })
  public ResponseEntity<ProductoModel> actualizarProducto(@PathVariable Long id, 
    @RequestBody @Valid Producto productoActualizado) {
    try {
        Producto existente = productoService.buscarPorId(id);
        if (existente != null) {
            existente.setNombre(productoActualizado.getNombre());
            existente.setPrecio(productoActualizado.getPrecio());
            existente.setStock(productoActualizado.getStock());
            Producto actualizado = productoService.guardar(existente);
            return ResponseEntity.ok(assembler.toModel(actualizado)); // 
        } else {
            return ResponseEntity.notFound().build(); //404 Not Found
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().build(); // 400 Bad Request
    }
}

  @GetMapping("/{id}")
  @Operation(summary = "Obtener un producto por ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto encontrado",
      content = @Content(mediaType = "application/json", 
                          schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
  })
  public ResponseEntity<ProductoModel> obtenerProductoPorId(@PathVariable Long id) {
    try {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(assembler.toModel(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().build(); // en vez de 500
    }
}
  
  @DeleteMapping("/eliminar/{id}")
  @Operation(summary = "Eliminar un producto por ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
  })
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
  @Operation(
    summary = "Rebajar stock de un producto",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "JSON con la cantidad a rebajar (ejemplo: { \"cantidad\": 3 })",
        required = true,
        content = @Content(
            schema = @Schema(
                example = "{\"cantidad\": 5}"))
     )
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
  })
  public ResponseEntity<?> rebajarStock(@PathVariable Long id,
         @RequestBody Map<String, Integer> body) {
    try {
        int cantidad = body.get("cantidad");
        Producto actualizado = productoService.rebajarStock(id, cantidad);
        ProductoModel productoModel = assembler.toModel(actualizado);

        return ResponseEntity.ok(EntityModel.of(Map.of(
            "mensaje", "Stock actualizado correctamente",
            "producto", productoModel
        )
        )); // 200 con links
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // 400 Bad Request
    }
}
 
/*EndPoint para reponer unidades al inventario */
  @PatchMapping("/reponer/{id}")
  @Operation(summary = "Reponer stock de un producto")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock repuesto correctamente"),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
  })
  public ResponseEntity<?> reponerStock(
        @Parameter(description = "ID del producto a reponer", required = true)
        @PathVariable Long id,
        @Parameter(description = "Cantidad a reponer", required = true, content= @Content(
            examples = @ExampleObject(value = "5")
        ))
        @RequestParam int cantidad) {

    try {
        productoService.reponerStock(id, cantidad);
        Producto actualizado = productoService.buscarPorId(id);// Busqueda para mostrar el producto acutalizado
        ProductoModel model = assembler.toModel(actualizado);
      
        return ResponseEntity.ok(
          Map.of("Mensaje", "Stock repuesto correctamente.", "Producto", model)); // 200 OK
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
    }
}

  /*EndPoint para buscar por nombre de producto*/
  @GetMapping("/buscar")
  @Operation(summary = "Buscar productos por nombre", parameters = {
    @Parameter(name = "nombre", description = "Nombre, o parte de este, del producto a buscar", required = true)
  })
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos encontrados"),
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
  })
  public ResponseEntity<List<ProductoModel>> buscarPorNombre(@RequestParam String nombre) {
    try  {
      List<Producto> productos = productoService.buscarPorNombre(nombre);

      //Transformar cada producto a su versión HATEOAS
      List<ProductoModel> modelos = productos.stream()
          .map(assembler::toModel)
          .toList();
      return ResponseEntity.ok(modelos); // 200 OK
      
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(null); // 400 Bad Request
    }
}
  
  /*Endpoint para stock bajo */
  @GetMapping("/stock/bajo/{cantidad}")
  @Operation(summary = "Listar productos con stock bajo")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos con stock bajo obtenidos correctamente"),
    @ApiResponse(responseCode = "404", description = "No se encontraron productos con stock bajo")
  })
  public ResponseEntity<List<ProductoModel>> obtenerProductosConStockBajo(@PathVariable int cantidad) {
    List<Producto> productos = productoService.listarStockBajo(cantidad);
    List<ProductoModel> modelos = productos.stream()
        .map(assembler::toModel)
        .toList();
    return ResponseEntity.ok(modelos); // 200 OK
}
  
  /*EndPoint para mostrar productos cuyo precio sea menor a cierta cantidad */
  @GetMapping("/precio/menor/{precio}")
  @Operation(summary = "Listar productos con precio menor a")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos con precio menor a obtenidos correctamente"),
    @ApiResponse(responseCode = "404", description = "No se encontraron productos con precio menor a")
  })
  public ResponseEntity<List<ProductoModel>> obtenerProductosPorPrecioMenor(@PathVariable double precio) {
    try {
      List<Producto> productos = productoService.listarPorPrecioMenorA(precio);
      List<ProductoModel> modelos = productos.stream()
          .map(assembler::toModel)
          .toList();
      return ResponseEntity.ok(modelos); // 200 OK
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(null); // 400 Bad Request
    }
}
  
 /*EndPoint que devuelve los productos sin STOCK */
  @GetMapping("/sin-stock")
  @Operation(summary = "Listar productos sin stock")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos sin stock obtenidos correctamente",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "No se encontraron productos sin stock")
  })
  public ResponseEntity<List<ProductoModel>> obtenerProductosSinStock() {
    List<Producto> productosSinStock = productoService.listarStockBajo(1);
    List<ProductoModel> modelos = productosSinStock.stream()
        .map(assembler::toModel)
        .toList();
    return ResponseEntity.ok(modelos); // 200 OK
  }
  
  /*EndPoint que devuelve los productos con stock */
  @GetMapping("/con-stock")
  @Operation(summary = "Listar productos con stock")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos con stock obtenidos correctamente",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "No se encontraron productos con stock")
  })
  public ResponseEntity<List<ProductoModel>> obtenerProductosConStock() {
    List<Producto> productosConStock = productoService.listarConStock();
    List<ProductoModel> modelos = productosConStock.stream()
        .map(assembler::toModel)
        .toList();
    return ResponseEntity.ok(modelos); // 200 OK
}

/*---------Endpoint Reportes----------- */

  /*Este endpoint devuelve el stock total de todos los productos en la base de datos. */
  @GetMapping("/stock/total")
  @Operation(summary = "Obtener stock total de productos")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock total obtenido correctamente",
      content = @Content(mediaType = "application/json",
        schema = @Schema(type = "integer", example = "100"))),
    @ApiResponse(responseCode = "400", description = "Error al obtener el stock total")
  })
  public ResponseEntity<Integer> obtenerStockTotal() {
    try {
        Integer total = productoService.obtenerStockTotal();
        return ResponseEntity.ok(total != null ? total : 0); // 200 OK
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(0); // 400 Bad Request con valor 0
    }
}

  /*Este endpoint devuelve un resumen del inventario, incluyendo el stock total y la cantidad de productos. */
  @GetMapping("/reporte/resumen")
  @Operation(summary = "Obtener resumen del inventario")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Resumen del inventario obtenido correctamente",
      content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Map.class))),
    @ApiResponse(responseCode = "400", description = "Error al obtener el resumen del inventario")
  })
  public ResponseEntity<Map<String, Object>> obtenerResumen() {
    try {
        Map<String, Object> resumen = productoService.obtenerResumenInventario();
        return ResponseEntity.ok(resumen != null ? resumen : Map.of()); // 200 OK
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "No se pudo generar el resumen del inventario"
        )); // 400 Bad Request con mensaje controlado
    }
  }
}