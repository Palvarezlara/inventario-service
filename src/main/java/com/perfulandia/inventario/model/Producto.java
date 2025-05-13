package com.perfulandia.inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
   @Id
   @GeneratedValue
   private Long id;

   @Column(nullable = false)
   private String nombre;

   @Column(nullable = false)
   private int stock;
   
   @Column(nullable = false)
   private double precio;
}
