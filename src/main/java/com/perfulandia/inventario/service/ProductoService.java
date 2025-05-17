package com.perfulandia.inventario.service;

import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.model.Producto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



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

    /* Metodo que verifique que existe el producto y que lo reponga al inventario */
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
   
    /*Metodo para obtener el stock total */
    // Este método devuelve el stock total de todos los productos
    public Integer obtenerStockTotal() {
    return repository.obtenerStockTotal();
    }

    /*Metodo para obtener el resumen del inventario */
    // Este método devuelve un resumen del inventario, incluyendo el stock total y la cantidad de productos
    public Map<String, Object> obtenerResumenInventario() {
    List<Producto> productos = repository.findAll();

    int totalStock = productos.stream().mapToInt(Producto::getStock).sum();
    double valorInventario = productos.stream()
            .mapToDouble(p -> p.getStock() * p.getPrecio())
            .sum();

    Map<String, Object> resultado = new HashMap<>();
    resultado.put("productos", productos);
    resultado.put("totalStock", totalStock);
    resultado.put("valorInventario", valorInventario);

    return resultado;
}

    /*Metodo que devuelve todos los productos sin stock */
    public List<Producto> listarSinStock() {
        return repository.findByStockLessThan(1);
    }

    /*Metodo que devuelve todos los productos con stock */
    // Este método devuelve una lista de productos cuyo stock es mayor a 0
    public List<Producto> listarConStock() {
    return repository.findByStockGreaterThan(0);
    }


}
