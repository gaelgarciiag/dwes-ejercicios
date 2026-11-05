-- Pregunta 1: Insertar un producto
INSERT INTO producto (nombre, precio)
VALUES ('Auriculares', 39.95);

SELECT LAST_INSERT_ID();
SELECT * FROM producto;

-- Pregunta 2: Productos de más de 20 €, de mayor a menor precio
SELECT nombre, precio
FROM producto
WHERE precio > 20
ORDER BY precio DESC;

-- Pregunta 3: Contar los pedidos del cliente 1
SELECT COUNT(*) AS total_pedidos
FROM pedido
WHERE cliente_id = 1;

-- Pregunta 4: Pedidos del cliente con email ana@ejemplo.es
SELECT pedido.id, pedido.fecha
FROM pedido
INNER JOIN cliente
ON pedido.cliente_id = cliente.id
WHERE cliente.email = 'ana@ejemplo.es';

-- Pregunta 5: Productos y cantidades del pedido 1
SELECT producto.nombre, linea_pedido.cantidad
FROM linea_pedido
INNER JOIN producto
ON linea_pedido.producto_id = producto.id
WHERE linea_pedido.pedido_id = 1;

-- Pregunta 6: Calcular el total de cada pedido
SELECT linea_pedido.pedido_id,
       SUM(linea_pedido.cantidad * producto.precio) AS total
FROM linea_pedido
INNER JOIN producto
ON linea_pedido.producto_id = producto.id
GROUP BY linea_pedido.pedido_id
ORDER BY linea_pedido.pedido_id;

-- Pregunta 7: Subir un 10 % los productos de menos de 20 €
UPDATE producto
SET precio = ROUND(precio * 1.10, 2)
WHERE precio < 20;

-- Comprobar el nuevo precio del Ratón
SELECT nombre, precio
FROM producto
WHERE nombre = 'Ratón';

-- Pregunta 8: Eliminar Auriculares
DELETE FROM producto
WHERE nombre = 'Auriculares';
SELECT * FROM producto;

-- Pregunta 9: Intentar eliminar un producto relacionado con pedidos
DELETE FROM producto
WHERE id = 1;

-- Pregunta 10: Transacciones

START TRANSACTION;

UPDATE producto
SET precio = 0;

SELECT * FROM producto;

ROLLBACK;

SELECT * FROM producto;