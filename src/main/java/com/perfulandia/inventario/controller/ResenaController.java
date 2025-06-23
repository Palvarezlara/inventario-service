package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.inventario.model.Resena;
import com.perfulandia.inventario.service.ResenaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/resenas")
@Tag(name = "Reseñas", description = "Operaciones relacionadas con las reseñas de productos")
public class ResenaController {
    
    @Autowired // Autowired es una anotación de Spring que permite inyectar dependencias automáticamente.
    private ResenaService resenaService; // Instancia del servicio de reseñas   


    //--------------------ENDPOINTS CRUD----------------------------//
    @PostMapping("/crear")
    @Operation(summary = "Crear una reseña", 
            description = "Permite crear una nueva reseña para un producto",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto Reseña con los datos de la reseña a crear",
            required= true,
            content = @Content(schema = @Schema (implementation = Resena.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class)) ),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<Resena> guardarResena(@Valid @RequestBody Resena resena) {
        
        Resena resenaGuardada = resenaService.guardar(resena);
        return ResponseEntity.status(201).body(resenaGuardada); // Retorna 201 Created con la reseña guardada
    }

    // Listar todas las reseñas
    @GetMapping("/all")
    @Operation(summary = "Listar todas las reseñas", 
            description = "Obtiene una lista de todas las reseñas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente (posiblemente vacía)",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<List<Resena>> listarResenas() {
        try {
            List<Resena> resenas = resenaService.listar();
            return ResponseEntity.ok(resenas); // Retorna 200 OK con la lista de reseñas
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }

    //Buscar una reseña por ID
    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerResenaPorId(@PathVariable Long id) {
        return resenaService.buscarPorId(id)
                .map(ResponseEntity::ok) // Si se encuentra la reseña, retorna 200 OK con la reseña
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, retorna 404 Not Found
    }

    // Listar reseñas por usuario ID
    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar reseñas por usuario", 
            description = "Obtiene una lista de reseñas realizadas por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas del usuario obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<List<Resena>> listarPorUsuario(@PathVariable Long idUsuario) {
        try{
            List<Resena> resenas = resenaService.listarPorUsuarioId(idUsuario);
            return ResponseEntity.ok(resenas); // Retorna 200 OK con la lista de reseñas del usuario
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }

    // Actualizar una reseña
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reseña", 
            description = "Permite actualizar los datos de una reseña existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto Reseña con los datos actualizados de la reseña",
            required= true,
            content = @Content(schema = @Schema (implementation = Resena.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    public ResponseEntity<Resena> actualizarResena(@PathVariable Long id, @Valid @RequestBody Resena resenaActualizada) {
        try {
            if (!id.equals(resenaActualizada.getId())) {
                return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request si el ID no coincide
            }
            return resenaService.buscarPorId(id).map(resenaExistente -> {
                resenaExistente.setComentario(resenaActualizada.getComentario());
                resenaExistente.setCalificacion(resenaActualizada.getCalificacion());
                resenaExistente.setNombreUsuario(resenaActualizada.getNombreUsuario());
                return ResponseEntity.ok(resenaService.actualizar(resenaExistente)); // Retorna 200 OK con la reseña actualizada
            }).orElse(ResponseEntity.notFound().build()); // Si no se encuentra la reseña, retorna 404 Not Found
        
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }

    // Eliminar una reseña
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reseña", 
            description = "Permite eliminar una reseña existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        if (resenaService.buscarPorId(id).isPresent()) {
            resenaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content si la reseña fue eliminada exitosamente
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found si la reseña no existe
        }
    }

    // Listar reseñas por producto ID
    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Listar reseñas por producto", 
            description = "Obtiene una lista de reseñas realizadas para un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas del producto obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<List<Resena>> listarPorProducto(@PathVariable Long idProducto) {
        try {
            List<Resena> resenas = resenaService.listarPorProductoId(idProducto);
            return ResponseEntity.ok(resenas); // Retorna 200 OK con la lista de reseñas del producto
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }


    //---------LOGICA DE NEGOCIO----------------

    // 🔹 Obtener promedio de calificación de un producto
    @GetMapping("/producto/promedio/{idProducto}")
    @Operation(summary = "Obtener promedio de calificación de un producto", 
            description = "Calcula el promedio de calificación de un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promedio de calificación obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = Double.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida o producto no encontrado")
    })
    public ResponseEntity<Double> obtenerPromedioCalificacion(@PathVariable Long idProducto) {
        try {
            Double promedio = resenaService.obtenerPromedioCalificacion(idProducto);
            return ResponseEntity.ok(promedio != null ? promedio : 0.0); // Retorna 200 OK con el promedio de calificación, o 0.0 si no hay reseñas
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }

    // 🔹 Reporte: Cantidad de reseñas por producto
    @GetMapping("/reporte/cantidad-por-producto")
    @Operation(summary = "Reporte de cantidad de reseñas por producto", 
            description = "Obtiene un reporte con la cantidad de reseñas realizadas por cada producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte de cantidad de reseñas por producto obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<List<Map<String, Object>>> reporteCantidadPorProducto() {
        try {
            List<Map<String, Object>> reporte = resenaService.obtenerCantidadResenasPorProducto();
            return ResponseEntity.ok(reporte); // Retorna 200 OK con el reporte de cantidad de reseñas por producto
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }


}
