package com.perfulandia.inventario.assemblers;

import com.perfulandia.inventario.model.Resena;
import com.perfulandia.inventario.controller.ResenaController;
import com.perfulandia.inventario.dto.ResenaModel;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;


@Component
public class ResenaModelAssembler implements RepresentationModelAssembler<Resena, ResenaModel> {

    @Override
    @org.springframework.lang.NonNull
    public ResenaModel toModel(Resena resena) {
        ResenaModel model = new ResenaModel();
        model.setId(resena.getId());
        model.setComentario(resena.getComentario());
        model.setCalificacion(resena.getCalificacion());
        model.setIdProducto(resena.getIdProducto());
        model.setIdUsuario(resena.getIdUsuario());
        model.setNombreUsuario(resena.getNombreUsuario());

        model.add(linkTo(methodOn(ResenaController.class)
                .obtenerResenaPorId(resena.getId())).withSelfRel());

        model.add(linkTo(methodOn(ResenaController.class)
                .listarResenas()).withRel("todas-las-resenas"));

        return model;
    }
    
}
