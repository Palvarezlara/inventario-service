package com.perfulandia.inventario.service;

import org.springframework.stereotype.Service;
import com.perfulandia.inventario.repository.ResenaRepository;
import com.perfulandia.inventario.model.Resena;

import java.util.*;


@Service
public class ResenaService {
    
    private final ResenaRepository resenaRepository;

    /*------------CRUD---------- */

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public Resena guardar(Resena resena) {
        return resenaRepository.save(resena);
    }

    public List<Resena> listar() {
        return resenaRepository.findAll();
    }

    public List<Resena> listarPorProductoId(Long idProducto) {
    return resenaRepository.findByIdProducto(idProducto);
    }

    public List<Resena> listarPorUsuarioId(Long usuarioId) {
    return resenaRepository.findByIdUsuario(usuarioId);
    }

    public Optional<Resena> buscarPorId(Long id) {
        return resenaRepository.findById(id);
    }

    public void eliminar(Long id) {
        resenaRepository.deleteById(id);
    }

    public Resena actualizar(Resena resena) {
        return resenaRepository.save(resena);
    }  

    public Double obtenerPromedioCalificacion(Long productoId) {
    return resenaRepository.promedioCalificacionPorProducto(productoId);
    }

    public List<Map<String, Object>> obtenerCantidadResenasPorProducto() {
        List<Object[]> resultados = resenaRepository.cantidadResenasPorProducto();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("idProducto", fila[0]);
            map.put("cantidadResenas", fila[1]);
            respuesta.add(map);
    }

        return respuesta;
    }




    
}
