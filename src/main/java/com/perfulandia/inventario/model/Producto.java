package com.perfulandia.inventario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "producto")
@AllArgsConstructor
@NoArgsConstructor

public class Producto {

   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String nombre;

   @Column(nullable = false)
   private int stock;
   
   @Column(nullable = false)
   private double precio;

   
}