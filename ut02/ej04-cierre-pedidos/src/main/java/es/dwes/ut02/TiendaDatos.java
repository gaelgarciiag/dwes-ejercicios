package es.dwes.ut02;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Acceso a los datos de la tienda: clientes, pedidos y líneas de pedido.
 *
 * <p>Reglas del ejercicio:
 * <ul>
 *   <li>Todas las sentencias usan parámetros {@code ?}: nunca se concatena SQL.</li>
 *   <li>Conexión, sentencia y resultado se abren con {@code try-with-resources}.</li>
 *   <li>Los métodos que tocan varias tablas usan <b>una transacción</b>: o se hace todo o nada.</li>
 * </ul>
 *
 * <p>didactico: una clase por tabla (patrón DAO) se verá en la UT3; aquí, todo junto
 * para centrarnos en JDBC.
 */
public class TiendaDatos {

    private final String url;
    private final String usuario;
    private final String clave;

    public TiendaDatos(String url, String usuario, String clave) {
        this.url = url;
        this.usuario = usuario;
        this.clave = clave;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }

    // ---- Clientes -------------------------------------------------------

    /** Crea un cliente y devuelve su id. El email no puede repetirse (lanza SQLException). */
    
public int crearCliente(String nombre, String email) throws SQLException {
    String sql = "INSERT INTO cliente (nombre, email) VALUES (?, ?)";

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(
             sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, nombre);
        ps.setString(2, email);

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }

    throw new SQLException("No se ha generado ningún ID");
}

    /** Devuelve el cliente con ese email, o vacío si no existe. */
    
public Optional<Cliente> buscarClientePorEmail(String email) throws SQLException {
    String sql = "SELECT id, nombre, email FROM cliente WHERE email = ?";

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, email);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email")
                );

                return Optional.of(cliente);
            }
        }
    }

    return Optional.empty();
}

    // ---- Pedidos --------------------------------------------------------

    /**
     * Crea un pedido con todas sus líneas y devuelve el id del pedido.
     *
     * @param lineas mapa {@code idProducto -> cantidad}
     * @throws SQLException si algo falla (producto inexistente, cantidad 0...).
     *         En ese caso <b>no debe quedar nada guardado</b>: ni el pedido ni ninguna línea.
     */
    
public int crearPedidoConLineas(int clienteId, LocalDateTime fecha, Map<Integer, Integer> lineas)
        throws SQLException {

    String sqlPedido = "INSERT INTO pedido (cliente_id, fecha) VALUES (?, ?)";
    String sqlLinea = "INSERT INTO linea_pedido (pedido_id, producto_id, cantidad) VALUES (?, ?, ?)";

    try (Connection con = conectar()) {
        con.setAutoCommit(false);

        try {
            int pedidoId;

            // 1. Crear el pedido
            try (PreparedStatement ps = con.prepareStatement(
                    sqlPedido, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, clienteId);
                ps.setTimestamp(2, Timestamp.valueOf(fecha));
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("No se ha generado el ID del pedido");
                    }
                    pedidoId = rs.getInt(1);
                }
            }

            // 2. Insertar las líneas del pedido
            try (PreparedStatement ps = con.prepareStatement(sqlLinea)) {

                for (Map.Entry<Integer, Integer> linea : lineas.entrySet()) {
                    ps.setInt(1, pedidoId);
                    ps.setInt(2, linea.getKey());
                    ps.setInt(3, linea.getValue());

                    ps.executeUpdate();
                }
            }

            // 3. Confirmar todos los cambios
            con.commit();

            return pedidoId;

        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        }
    }
}

    /** Pedidos de un cliente, localizado por su email (JOIN), ordenados por id. */
    
public List<Pedido> pedidosDeCliente(String email) throws SQLException {
    String sql = """
        SELECT p.id, p.cliente_id, p.fecha
        FROM pedido p
        INNER JOIN cliente c ON p.cliente_id = c.id
        WHERE c.email = ?
        ORDER BY p.id
        """;

    List<Pedido> pedidos = new ArrayList<>();

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, email);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id"),
                    rs.getInt("cliente_id"),
                    rs.getTimestamp("fecha").toLocalDateTime()
                );

                pedidos.add(pedido);
            }
        }
    }

    return pedidos;
}

    /** Total del pedido: suma de cantidad x precio. Si no existe o no tiene líneas, cero. */
    
public BigDecimal totalPedido(int pedidoId) throws SQLException {
    String sql = """
        SELECT COALESCE(SUM(lp.cantidad * pr.precio), 0) AS total
        FROM linea_pedido lp
        INNER JOIN producto pr ON lp.producto_id = pr.id
        WHERE lp.pedido_id = ?
        """;

    try (Connection con = conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, pedidoId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        }
    }

    return BigDecimal.ZERO;
}

    /**
     * Cancela un pedido: borra sus líneas y después el pedido, en una sola transacción.
     * Devuelve {@code true} si el pedido existía.
     */
    
public boolean cancelarPedido(int pedidoId) throws SQLException {
    String sqlLineas = "DELETE FROM linea_pedido WHERE pedido_id = ?";
    String sqlPedido = "DELETE FROM pedido WHERE id = ?";

    try (Connection con = conectar()) {
        con.setAutoCommit(false);

        try {
            // 1. Eliminar las líneas del pedido
            try (PreparedStatement ps = con.prepareStatement(sqlLineas)) {
                ps.setInt(1, pedidoId);
                ps.executeUpdate();
            }

            // 2. Eliminar el pedido
            int filasAfectadas;

            try (PreparedStatement ps = con.prepareStatement(sqlPedido)) {
                ps.setInt(1, pedidoId);
                filasAfectadas = ps.executeUpdate();
            }

            // 3. Confirmar los cambios
            con.commit();

            return filasAfectadas > 0;

        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        }
    }
}
}
