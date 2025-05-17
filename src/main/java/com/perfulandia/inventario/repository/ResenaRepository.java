package com.perfulandia.inventario.repository;

import com.perfulandia.inventario.model.Resena;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, buscar reseñas por producto o usuario
    List<Resena> findByIdProducto(Long idProducto);

    List<Resena> findByIdUsuario(Long idUsuario); 

    List<Resena> findByCalificacion(int calificacion);

    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.idProducto = :productoId")
    Double promedioCalificacionPorProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT r.idProducto, COUNT(r) FROM Resena r GROUP BY r.idProducto")
    List<Object[]> cantidadResenasPorProducto();


}
