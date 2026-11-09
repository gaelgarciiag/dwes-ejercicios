package es.dwes.ut03;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * {@link ProductoDao} que guarda los productos en un mapa, sin base de datos. Sirve para
 * probar la lógica de negocio sin arrancar ningún servidor.
 */
public class ProductoDaoMemoria implements ProductoDao {

    /** Ordenado por id, como el {@code ORDER BY id} de SQL. */
    private final Map<Integer, Producto> productos = new TreeMap<>();
    private int siguienteId = 1;

    @Override
    public List<Producto> listar() {
        // TODO 8a: copia de los valores del mapa (el llamante no debe poder modificarlo)
        throw new UnsupportedOperationException("TODO 8a");
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        // TODO 8b
        throw new UnsupportedOperationException("TODO 8b");
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        // TODO 8c: coincidencia exacta, en orden de id
        throw new UnsupportedOperationException("TODO 8c");
    }

    @Override
    public int insertar(String nombre, BigDecimal precio) {
        // TODO 8d: asigna siguienteId (y súbelo), guarda el producto y devuelve su id
        throw new UnsupportedOperationException("TODO 8d");
    }

    @Override
    public boolean actualizarPrecio(int id, BigDecimal nuevoPrecio) {
        // TODO 8e: los records son inmutables: sustituye el producto por uno nuevo con el mismo id
        throw new UnsupportedOperationException("TODO 8e");
    }

    @Override
    public boolean eliminar(int id) {
        // TODO 8f
        throw new UnsupportedOperationException("TODO 8f");
    }
}
