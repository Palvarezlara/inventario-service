package com.perfulandia.inventario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.repository.ProductoRepository;

@SpringBootApplication
public class InventarioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioServiceApplication.class, args);
	}

	// Aquí puedes agregar más configuraciones o beans si es necesario
	@Bean
	public CommandLineRunner demo(ProductoRepository productoRepository) {
		return args -> {
			Producto p = new Producto();
            p.setNombre("Aceite de masaje");
            p.setStock(25);
            p.setPrecio(4990.0);

            productoRepository.save(p);
            System.out.println("✅ Producto guardado correctamente."
			);
		};
	}
}
