
DELETE FROM producto;

-- Script para poblar la tabla 'producto'

INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Floral', 10, 19990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Amaderado', 5, 25990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Citrico', 8, 14990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Dulce', 12, 17990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Fresco', 7, 21990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Oriental', 3, 29990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Verde', 6, 23990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume Deportivo', 4, 15990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume de Noche', 2, 24990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume de Dia', 9, 18990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume de Verano', 11, 20990.0);
INSERT INTO producto (nombre, stock, precio) VALUES ('Perfume de Invierno', 1, 26990.0);


INSERT INTO resena (comentario, calificacion, id_producto, id_usuario, nombre_usuario)
VALUES ('Muy buen aroma', 5, 1, 101, 'Ana Lopez');

INSERT INTO resena (comentario, calificacion, id_producto, id_usuario, nombre_usuario)
VALUES ('Me encanto', 4, 12, 102, 'Luis Garcia');

INSERT INTO resena (comentario, calificacion, id_producto, id_usuario, nombre_usuario)
VALUES ('No me duro mucho', 3, 2, 103, 'Marta Soto');

INSERT INTO resena (comentario, calificacion, id_producto, id_usuario, nombre_usuario)
VALUES ('Perfecto para el dia', 5, 3, 104, 'Carlos Ruiz');

INSERT INTO resena (comentario, calificacion, id_producto, id_usuario, nombre_usuario)
VALUES ('Un poco fuerte', 2, 6, 105, 'Paula Diaz');


