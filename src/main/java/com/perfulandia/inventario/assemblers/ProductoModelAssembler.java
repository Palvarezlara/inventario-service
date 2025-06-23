package com.perfulandia.inventario.assemblers;

import com.perfulandia.inventario.dto.ProductoModel;
import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.controller.ProductoController;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, ProductoModel> {

    @Override
    @org.springframework.lang.NonNull
    public ProductoModel toModel(Producto producto) {
        ProductoModel model = new ProductoModel();
        model.setId(producto.getId());
        model.setNombre(producto.getNombre());
        model.setStock(producto.getStock());
        model.setPrecio(producto.getPrecio());

        model.add(linkTo(methodOn(ProductoController.class).
        obtenerProductoPorId(producto.getId())).withSelfRel());

        model.add(linkTo(methodOn(ProductoController.class).
        listarProductos()).withRel("todos-los-productos"));

        return model;
    }
}