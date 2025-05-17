package com.perfulandia.inventario.controller;

import com.perfulandia.inventario.model.Resena;
import com.perfulandia.inventario.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {
    
    @Autowired // Autowired es una anotación de Spring que permite inyectar dependencias automáticamente.
    private ResenaService resenaService; // Instancia del servicio de reseñas   


    // Crear una reseña
    @PostMapping
    public ResponseEntity<Resena> guardarResena(@Valid @RequestBody Resena resena) {
        return ResponseEntity.ok(resenaService.guardar(resena));
    }

    // Listar todas las reseñas
    @GetMapping
    public ResponseEntity<List<Resena>> listarResenas() {
        return ResponseEntity.ok(resenaService.listar());
    }
    
    //Buscar una reseña por ID
    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerResenaPorId(@PathVariable Long id) {
        return resenaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar reseñas por usuario ID
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Resena>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<Resena> resenas = resenaService.listarPorUsuarioId(idUsuario);
        return ResponseEntity.ok(resenas);
    }

    // Actualizar una reseña
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(@PathVariable Long id, @Valid @RequestBody Resena resenaActualizada) {
        return resenaService.buscarPorId(id).map(resenaExistente -> {
            resenaExistente.setComentario(resenaActualizada.getComentario());
            resenaExistente.setCalificacion(resenaActualizada.getCalificacion());
            resenaExistente.setNombreUsuario(resenaActualizada.getNombreUsuario());
            return ResponseEntity.ok(resenaService.actualizar(resenaExistente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una reseña
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        if (resenaService.buscarPorId(id).isPresent()) {
            resenaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar reseñas por producto ID
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Resena>> listarPorProducto(@PathVariable Long idProducto) {
        List<Resena> resenas = resenaService.listarPorProductoId(idProducto);
        return ResponseEntity.ok(resenas);
    }


    //---------LOGICA DE NEGOCIO----------------

    // 🔹 Obtener promedio de calificación de un producto
    @GetMapping("/producto/{idProducto}/promedio")
    public ResponseEntity<Double> obtenerPromedioCalificacion(@PathVariable Long idProducto) {
        Double promedio = resenaService.obtenerPromedioCalificacion(idProducto);
        return ResponseEntity.ok(promedio != null ? promedio : 0.0);
    }

    // 🔹 Reporte: Cantidad de reseñas por producto
    @GetMapping("/reporte/cantidad-por-producto")
        public ResponseEntity<List<Map<String, Object>>> reporteCantidadPorProducto() {
        return ResponseEntity.ok(resenaService.obtenerCantidadResenasPorProducto());
    }


}
