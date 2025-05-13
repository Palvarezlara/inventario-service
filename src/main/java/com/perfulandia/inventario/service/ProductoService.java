package com.perfulandia.inventario.service;

import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.model.Producto;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public Producto buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }    

}
