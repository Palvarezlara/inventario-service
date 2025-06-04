package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importa la anotación Autowired
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.inventario.model.Resena;
import com.perfulandia.inventario.service.ResenaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/resenas")
public class ResenaController {
    
    @Autowired // Autowired es una anotación de Spring que permite inyectar dependencias automáticamente.
    private ResenaService resenaService; // Instancia del servicio de reseñas   


    // Crear una reseña
    @PostMapping("/crear")
    public ResponseEntity<Resena> guardarResena(@Valid @RequestBody Resena resena) {
        Resena resenaGuardada = resenaService.guardar(resena);
        return ResponseEntity.status(201).body(resenaGuardada); // Retorna 201 Created con la reseña guardada
    }

    // Listar todas las reseñas
    @GetMapping("/all")
    public ResponseEntity<List<Resena>> listarResenas() {
        return ResponseEntity.ok(resenaService.listar()); // Retorna 200 OK con la lista de reseñas
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
    public ResponseEntity<List<Resena>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<Resena> resenas = resenaService.listarPorUsuarioId(idUsuario);
        return ResponseEntity.ok(resenas); // Retorna 200 OK con la lista de reseñas del usuario
    }

    // Actualizar una reseña
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(@PathVariable Long id, @Valid @RequestBody Resena resenaActualizada) {
        return resenaService.buscarPorId(id).map(resenaExistente -> {
            resenaExistente.setComentario(resenaActualizada.getComentario());
            resenaExistente.setCalificacion(resenaActualizada.getCalificacion());
            resenaExistente.setNombreUsuario(resenaActualizada.getNombreUsuario());
            return ResponseEntity.ok(resenaService.actualizar(resenaExistente)); // Retorna 200 OK con la reseña actualizada
        }).orElse(ResponseEntity.notFound().build()); // Si no se encuentra la reseña, retorna 404 Not Found
    }

    // Eliminar una reseña
    @DeleteMapping("/{id}")
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
    public ResponseEntity<List<Resena>> listarPorProducto(@PathVariable Long idProducto) {
        List<Resena> resenas = resenaService.listarPorProductoId(idProducto);
        return ResponseEntity.ok(resenas); // Retorna 200 OK con la lista de reseñas del producto
    }


    //---------LOGICA DE NEGOCIO----------------

    // 🔹 Obtener promedio de calificación de un producto
    @GetMapping("/producto/promedio/{idProducto}")
    public ResponseEntity<Double> obtenerPromedioCalificacion(@PathVariable Long idProducto) {
        Double promedio = resenaService.obtenerPromedioCalificacion(idProducto);
        return ResponseEntity.ok(promedio != null ? promedio : 0.0); // Retorna 200 OK con el promedio de calificación, o 0.0 si no hay reseñas
    }

    // 🔹 Reporte: Cantidad de reseñas por producto
    @GetMapping("/reporte/cantidad-por-producto")
        public ResponseEntity<List<Map<String, Object>>> reporteCantidadPorProducto() {
        return ResponseEntity.ok(resenaService.obtenerCantidadResenasPorProducto()); // Retorna 200 OK con el reporte de cantidad de reseñas por producto
    }


}
