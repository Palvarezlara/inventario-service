package com.perfulandia.inventario.service;

import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.model.Producto;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class ProductoService {

    private final ProductoRepository repository;
//----------------------------CRUD----------------------------//
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
    
    public List<Producto> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    //-----------Logica de negocio----------------//

    /* metodo que verifique que existe el producto y que lo rebaje del inventario */
    public Producto rebajarStock(Long id, int cantidad) {
    Producto producto = repository.findById(id).orElse(null);

    if (producto == null) {
        throw new RuntimeException("Producto no encontrado con ID: " + id);
    }

    if (producto.getStock() < cantidad) {
        throw new RuntimeException("Stock insuficiente para el producto con ID: " + id);
    }

    producto.setStock(producto.getStock() - cantidad);
    return repository.save(producto);
    }

    /* metodo que verifique que existe el producto y que lo reponga al inventario */
    public void reponerStock(Long idProducto, int cantidad) {
    Producto producto = repository.findById(idProducto)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

    producto.setStock(producto.getStock() + cantidad);
    repository.save(producto);
    }
    /*Metodo para listar productos con stock bajo */
    public List<Producto> listarStockBajo(int cantidad) {
    return repository.findByStockLessThan(cantidad);
    }
    /*Metodo para listar productos con precio bajo */
    // Este método devuelve una lista de productos cuyo precio es menor al precio especificado    
    public List<Producto> listarPorPrecioMenorA(double precio) {
    return repository.findByPrecioLessThan(precio);
    }


}
