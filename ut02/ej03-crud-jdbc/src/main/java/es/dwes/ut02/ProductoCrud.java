package es.dwes.ut02;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CRUD de la tabla {@code producto} con JDBC.
 *
 * <p>Reglas del ejercicio:
 * <ul>
 *   <li>Cada método abre su conexión con {@code try-with-resources}.</li>
 *   <li>Los valores van siempre como parámetros {@code ?}: nunca se concatenan en el SQL.</li>
 * </ul>
 *
 * <p>didactico: abrir una conexión por método es una simplificación; el pool y las
 * transacciones se ven en el apunte 10 y el patrón DAO en la UT3.
 */
public class ProductoCrud {

    private final String url;
    private final String usuario;
    private final String clave;

    public ProductoCrud(String url, String usuario, String clave) {
        this.url = url;
        this.usuario = usuario;
        this.clave = clave;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }

    /** Inserta un producto y devuelve el id que le ha asignado la base de datos. */
    
public int insertar(String nombre, BigDecimal precio) throws SQLException {
    String sql = "INSERT INTO producto (nombre, precio) VALUES (?, ?)";

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(
             sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, nombre);
        ps.setBigDecimal(2, precio);

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }

    throw new SQLException("No se ha generado ningún ID");
}

    /** Devuelve el producto con ese id, o vacío si no existe. */
    
public Optional<Producto> buscarPorId(int id) throws SQLException {
    String sql = "SELECT id, nombre, precio FROM producto WHERE id = ?";

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("precio")
                );

                return Optional.of(producto);
            }
        }
    }

    return Optional.empty();
}


    /** Devuelve todos los productos, ordenados por id. */
    
public List<Producto> listar() throws SQLException {
    String sql = "SELECT id, nombre, precio FROM producto ORDER BY id";
    List<Producto> productos = new ArrayList<>();

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Producto producto = new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getBigDecimal("precio")
            );

            productos.add(producto);
        }
    }

    return productos;
}

    /** Devuelve los productos cuyo nombre es exactamente el indicado. */
    
public List<Producto> buscarPorNombre(String nombre) throws SQLException {
    String sql = "SELECT id, nombre, precio FROM producto WHERE nombre = ?";
    List<Producto> productos = new ArrayList<>();

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombre);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("precio")
                );

                productos.add(producto);
            }
        }
    }

    return productos;
}

    /** Cambia el precio. Devuelve {@code true} si existía el producto. */
   
public boolean actualizarPrecio(int id, BigDecimal nuevoPrecio) throws SQLException {
    String sql = "UPDATE producto SET precio = ? WHERE id = ?";

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setBigDecimal(1, nuevoPrecio);
        ps.setInt(2, id);

        int filasAfectadas = ps.executeUpdate();

        return filasAfectadas > 0;
    }
}

    /** Borra el producto. Devuelve {@code true} si existía. */
    
public boolean eliminar(int id) throws SQLException {
    String sql = "DELETE FROM producto WHERE id = ?";

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);

        int filasAfectadas = ps.executeUpdate();

        return filasAfectadas > 0;
    }
}
}
