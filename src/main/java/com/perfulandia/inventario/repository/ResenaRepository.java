package com.perfulandia.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.perfulandia.inventario.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, buscar reseñas por producto o usuario
    List<Resena> findByProductoId(Long idProducto);

    List<Resena> findByIdUsuario(Long idUsuario); 

    List<Resena> findByCalificacion(int calificacion);

    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.producto.id = :productoId")
    Double promedioCalificacionPorProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT r.producto.id, COUNT(r) FROM Resena r GROUP BY r.producto.id")
    List<Object[]> cantidadResenasPorProducto();


}
