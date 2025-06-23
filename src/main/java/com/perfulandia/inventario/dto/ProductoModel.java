package com.perfulandia.inventario.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoModel extends RepresentationModel<ProductoModel> {
    
    private Long id;
    private String nombre;
    private int stock;
    private double precio;
}
