
# 📦 Microservicio de Inventario y Reseñas - Perfulandia SPA

Este microservicio gestiona el inventario de productos, permite registrar reseñas de clientes y proporciona reportes útiles para la administración.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.5
- Maven
- MySQL (base de datos local)
- JPA/Hibernate

---

## 📁 Estructura de Carpetas

```
inventario/
├── controller/        # Controladores REST
├── service/           # Lógica de negocio
├── repository/        # Interfaces para acceso a datos
├── model/             # Entidades JPA
└── resources/
    ├── application-local.properties
    └── data.sql       # Datos de carga inicial
    └── schema.sql     # Creación de tablas (Momentaneo para el desarrollo)
```

---

## 🌐 Endpoints

### 🔹 CRUD Productos (`/api/productos`)
| Método | Endpoint                     | Descripción                          |
|--------|------------------------------|--------------------------------------|
| GET    | `/`                          | Lista todos los productos            |
| GET    | `/{id}`                      | Obtiene un producto por ID           |
| POST   | `/`                          | Crea un nuevo producto               |
| PUT    | `/{id}`                      | Actualiza un producto existente      |
| DELETE | `/{id}`                      | Elimina un producto por ID           |

### 🔹 Lógica de negocio Productos
| Método | Endpoint                     | Descripción                                        |
|--------|------------------------------|----------------------------------------------------|
| PATCH  | `/rebajarStock/{id}`         | Rebaja stock si hay cantidad suficiente            |
| PATCH  | `/reponer/{id}`              | Reponer stock de un producto                       |
| GET    | `/buscar?nombre=`            | Buscar producto por nombre (parcial o total)       |
| GET    | `/precio/menor/{precio}`     | Lista productos cuyo precio es menor al indicado   |
| GET    | `/stock/bajo/{cantidad}`     | Lista productos con stock menor o igual al número  |
| GET    | `/sin-stock`                 | Lista productos sin stock                          |
| GET    | `/con-stock`                 | Lista productos con stock disponible               |

### 📊 Reportes de Inventario
| Método | Endpoint                     | Descripción                                     |
|--------|------------------------------|-------------------------------------------------|
| GET    | `/reporte/resumen`           | Resumen de stock total y cantidad de productos  |
| GET    | `/stock/total`               | Devuelve la suma de stock de todos los productos|
---

### ✨ Reseñas (`/api/resenas`)
| Método | Endpoint                     | Descripción                                |
|--------|------------------------------|--------------------------------------------|
| GET    | `/`                          | Lista todas las reseñas                    |
| GET    | `/{id}`                      | Obtiene una reseña por ID                  |
| GET    | `/producto/{idProducto}`     | Reseñas por ID de producto                 |
| GET    | `/usuario/{idUsuario}`       | Reseñas por ID de usuario                  |
| POST   | `/`                          | Crea una reseña                            |
| PUT    | `/{id}`                      | Actualiza una reseña por ID                |
| DELETE | `/{id}`                      | Elimina una reseña por ID                  |

### 📊 Reportes de Reseñas
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

- Seguridad 
- Persistencia en la nube (MySQL en AWS)
- Dockers para desplegar el microservicio

---

## 🧪 Probar en Postman

Importa los siguientes endpoints y prueba el CRUD y reportes:
- http://localhost:8080/api/productos
- http://localhost:8080/api/resenas

---

## 📌 Autora
Pamela Alvarez – Duoc UC – Ingeniería en Informática – 2025

