package com.perfulandia.inventario.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaModel extends RepresentationModel<ResenaModel> {

    private Long id;
    private String comentario;
    private int calificacion;
    private Long idProducto;
    private Long idUsuario;
    private String nombreUsuario;

}
