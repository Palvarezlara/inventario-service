
# 📦 Microservicio de Inventario y Reseñas - Perfulandia SPA

Este proyecto es un microservicio desarrollado en Spring Boot para gestionar el inventario y reseñas de productos de la tienda Perfulandia SPA.
Incluye operaciones CRUD, lógica de negocio como rebaja y reposición de stock, reportes, y gestión de reseñas.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.5
- Maven
- Spring Data JPA
- MySQL (base de datos local)
- Postman
- Git
---

## 📁 Estructura de Carpetas

```
inventario/
├── controller/        # Controladores REST
|   ├── ProductoController.java
|   └── ResenaController.java
├── service/           # Lógica de negocio
|   ├── ProductoService.java
|   └── ResenaService.java
├── repository/        # Interfaces para acceso a datos
|   ├── ProductoRepository.java
|   └── ResenaRepository.java
├── model/             # Entidades JPA
|   ├── Producto.java
|   └── Resena.java
└── resources/
    ├── application.properties
    └── data.sql       # Datos de carga inicial
```
---

## 🌐 Endpoints 

### 🔹 CRUD Productos (`http://localhost:8080/api/productos`)
| Método | Endpoint                     | Descripción                          | Body Postman                                |
|--------|------------------------------|--------------------------------------|---------------------------------------------|
| GET    | `/api/productos`             | Lista todos los productos            |                                             |
| GET    | `/api/productos/{id}`        | Obtiene un producto por ID           |                                             |
| POST   | `/api/productos`             | Crea un nuevo producto               |{"nombre": "nombre","stock": ?,"precio": ? } |
| PUT    | `/api/productos/{id}`        | Actualiza un producto existente      |{"nombre": "nombre","stock": ?,"precio": ? } |
| DELETE | `/api/productos/{id}`        | Elimina un producto por ID           |                                             |

### 🔹 Lógica de negocio Productos (`/api/productos`)
| Método | Endpoint                     | Descripción                                        | Postman                              |
|--------|------------------------------|----------------------------------------------------|--------------------------------------|
| PATCH  | `/rebajarStock/{id}`         | Rebaja stock si hay cantidad suficiente            | Body:{ "cantidad": ?  }              |
| PATCH  | `/reponer/{id}?cantidad=xx`  | Reponer stock de un producto                       | parametro: `reponer/{id}?cantidad=xx`|
| GET    | `/buscar?nombre=xyz`         | Buscar producto por nombre (parcial o total)       |                                      |
| GET    | `/precio/menor/{precio}`     | Lista productos cuyo precio es menor al indicado   |                                      |
| GET    | `/stock/bajo/{cantidad}`     | Lista productos con stock menor o igual al número  |                                      |
| GET    | `/sin-stock`                 | Lista productos sin stock                          |                                      |
| GET    | `/con-stock`                 | Lista productos con stock disponible               |                                      |

### 📊 Reportes de Inventario
| Método | Endpoint                     | Descripción                                     |
|--------|------------------------------|-------------------------------------------------|
| GET    | `/reporte/resumen`           | Resumen de stock total y cantidad de productos  |
| GET    | `/stock/total`               | Devuelve la suma de stock de todos los productos|
---

### ✨ Reseñas (`http://localhost:8080/api/resenas`)
| Método | Endpoint                     | Descripción                 | Body Postman                                                       |
|--------|------------------------------|-----------------------------|--------------------------------------------------------------------|
| GET    | `/api/resenas`               | Lista todas las reseñas     |                                                                    |
| GET    | `/api/resenas/{id}`          | Obtiene una reseña por ID   |                                                                    |
| GET    | `/producto/{idProducto}`     | Reseñas por ID de producto  |                                                                    |
| GET    | `/usuario/{idUsuario}`       | Reseñas por ID de usuario   |                                                                    |
| POST   | `/api/resenas`               | Crea una reseña             |{"comentario": "huele delicioso", "calificacion": ?,"idProducto": ?,|
| PUT    | `/api/resenas/{id}`          | Actualiza una reseña por ID | idUsuario": ?,"nombreUsuario":"Esto es un nombre"}                 |
| DELETE | `/api/resenas/{id}`          | Elimina una reseña por ID   |                                                                    |

### 📊 Reportes de Reseñas (`/api/resenas`)
| Método | Endpoint                              | Descripción                                 |
|--------|---------------------------------------|---------------------------------------------|
| GET    | `/producto/promedio/{idProducto}`     | Promedio de calificación de un producto     |
| GET    | `/reporte/cantidad-por-producto`      | Cantidad total de reseñas por producto      |
---

## ⚠️ Validaciones implementadas

- `calificacion` de reseña entre 1 y 5
- Campos `nullable = false` en las entidades
- Manejo de errores con respuestas adecuadas (404, 400)

---

## 🔐 Consideraciones futuras

- Seguridad (autenticación/roles)
- Persistencia en la nube (MySQL en AWS)
- Dockers para desplegar el microservicio
- CI/CD (no aplica para esta entrega)
- Validaciones más robustas por campos (solo se aplicaron básicas)
- Interacción real entre microservicios (ej. carrito de compras)
- Pruebas

---

## 🧪 Probar en Postman

Importa los siguientes endpoints y prueba el CRUD y reportes:
- http://localhost:8080/api/productos
- http://localhost:8080/api/resenas
- Se adjunta archivo Json para facilitar pruebas en Postman (`src\main\resources\prodResena.json`)

---

## 📌 Autora
Pamela Alvarez – Desarrollo Fullstack 1 -
Duoc UC – Ingeniería en Informática – 2025

