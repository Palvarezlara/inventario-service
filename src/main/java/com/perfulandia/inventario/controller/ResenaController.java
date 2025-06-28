package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.perfulandia.inventario.assemblers.ResenaModelAssembler;

import com.perfulandia.inventario.dto.ResenaModel;  

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

    @Autowired
    private ResenaModelAssembler assembler;

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
    public ResponseEntity<ResenaModel> guardarResena(@Valid @RequestBody Resena resena) {
        
        Resena resenaGuardada = resenaService.guardar(resena);
        ResenaModel resenaModel = assembler.toModel(resenaGuardada); // Utiliza el ensamblador para convertir Resena a ResenaModel
        return ResponseEntity.created(linkTo(methodOn(ResenaController.class)
                .obtenerResenaPorId(resenaGuardada.getId())).toUri())
                .body(resenaModel); // Retorna 201 Created con la reseña creada
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
    public ResponseEntity<CollectionModel<ResenaModel>> listarResenas() {
        try {
            List<Resena> resenas = resenaService.listar();
            List<ResenaModel> resenasModel = resenas.stream()
                .map(assembler::toModel) // Utiliza el ensamblador para convertir Resena a ResenaModel
                .toList(); // Convierte la lista de Resena a ResenaModel usando el ensamblador
            return ResponseEntity.ok(CollectionModel.of(resenasModel, 
            linkTo(methodOn(ResenaController.class)
            .listarResenas()).withSelfRel())); // Retorna 200 OK con la lista de reseñas
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }
    }

    //Buscar una reseña por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reseña por ID", 
            description = "Permite obtener los detalles de una reseña específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")})
        
    public ResponseEntity<ResenaModel> obtenerResenaPorId(@PathVariable Long id) {
      try{
        Optional<Resena> resenaOptional = resenaService.buscarPorId(id);
        if (resenaOptional.isPresent()) {
            Resena resena = resenaOptional.get();
            return ResponseEntity.ok(assembler.toModel(resena)); // Retorna 200 OK con la reseña encontrada
        } else{
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found si no se encuentra la reseña
        }       
      } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request en caso de error
        }  

        
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
    public ResponseEntity<List<ResenaModel>> listarPorUsuario(@PathVariable Long idUsuario) {
        try{
            List<Resena> resenas = resenaService.listarPorUsuarioId(idUsuario);
            List<ResenaModel> resenasModel = resenas.stream()
                .map(assembler::toModel) 
                .toList(); 
            return ResponseEntity.ok(resenasModel); // Retorna 200 OK con la lista de reseñas del usuario
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
    public ResponseEntity<ResenaModel> actualizarResena(@PathVariable Long id, @Valid @RequestBody Resena resenaActualizada) {
        try {
            if (!id.equals(resenaActualizada.getId())) {
                return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request si el ID no coincide
            }
            return resenaService.buscarPorId(id).map(resenaExistente -> {
                resenaExistente.setComentario(resenaActualizada.getComentario());
                resenaExistente.setCalificacion(resenaActualizada.getCalificacion());
                resenaExistente.setNombreUsuario(resenaActualizada.getNombreUsuario());
                Resena actualizada = resenaService.actualizar(resenaExistente);
                return ResponseEntity.ok(assembler.toModel(actualizada)); // Retorna 200 OK con la reseña actualizada
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
    public ResponseEntity<List<ResenaModel>> listarPorProducto(@PathVariable Long idProducto) {
        try {
            List<Resena> resenas = resenaService.listarPorProductoId(idProducto);
            List<ResenaModel> resenasModel = resenas.stream()
                .map(assembler::toModel) 
                .toList();
            return ResponseEntity.ok(resenasModel); // Retorna 200 OK 
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400 
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
