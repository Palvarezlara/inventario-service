package com.perfulandia.inventario.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String nombre;

   @Column(nullable = false)
   private int stock;
   
   @Column(nullable = false)
   private double precio;

   @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Resena> resenas;
}