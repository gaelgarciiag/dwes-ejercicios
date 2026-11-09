package es.dwes.ut03;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * {@link ProductoDao} sobre MySQL con JDBC. Es el {@code ProductoCrud} de la UT2 convertido
 * en implementación de la interfaz.
 *
 * <p>Reglas: {@code try-with-resources}, parámetros {@code ?} y, si salta una
 * {@link SQLException}, se envuelve en {@link AccesoDatosException} (con la original como
 * causa) para que JDBC no se escape de esta clase.
 */
public class ProductoDaoJdbc implements ProductoDao {

    private final String url;
    private final String usuario;
    private final String clave;

    public ProductoDaoJdbc(String url, String usuario, String clave) {
        this.url = url;
        this.usuario = usuario;
        this.clave = clave;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }

    private static Producto aProducto(ResultSet rs) throws SQLException {
        return new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getBigDecimal("precio"));
    }

    @Override
    public List<Producto> listar() {
        // TODO 1: SELECT id, nombre, precio FROM producto ORDER BY id
        throw new UnsupportedOperationException("TODO 1");
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        // TODO 2: SELECT ... WHERE id = ?  (vacío si no hay fila)
        throw new UnsupportedOperationException("TODO 2");
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        // TODO 3: SELECT ... WHERE nombre = ? ORDER BY id  (con parámetro, nunca concatenado)
        throw new UnsupportedOperationException("TODO 3");
    }

    @Override
    public int insertar(String nombre, BigDecimal precio) {
        // TODO 4: INSERT con Statement.RETURN_GENERATED_KEYS y devolver el id generado
        throw new UnsupportedOperationException("TODO 4");
    }

    @Override
    public boolean actualizarPrecio(int id, BigDecimal nuevoPrecio) {
        // TODO 5: UPDATE producto SET precio = ? WHERE id = ?  (true si afectó a una fila)
        throw new UnsupportedOperationException("TODO 5");
    }

    @Override
    public boolean eliminar(int id) {
        // TODO 6: DELETE FROM producto WHERE id = ?  (true si afectó a una fila)
        throw new UnsupportedOperationException("TODO 6");
    }
}
