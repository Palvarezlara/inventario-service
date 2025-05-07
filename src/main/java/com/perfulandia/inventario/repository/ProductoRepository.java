package com.perfulandia.inventario.repository;

import com.perfulandia.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, buscar productos por nombre o precio
    // List<Producto> findByNombre(String nombre);

}
