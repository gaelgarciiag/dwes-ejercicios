package es.dwes.ut03;

/**
 * Fábrica de DAO: decide <b>qué implementación</b> se usa. El resto del programa pide un
 * {@link ProductoDao} y no sabe (ni le importa) cuál recibe.
 */
public final class DaoFactory {

    private DaoFactory() { }

    /**
     * @param modo {@code "jdbc"} o {@code "memoria"}
     * @param url, usuario, clave datos de conexión; solo se usan con {@code "jdbc"}
     * @throws IllegalArgumentException si el modo es nulo o desconocido
     */
    public static ProductoDao productoDao(String modo, String url, String usuario, String clave) {
        // TODO 9: "memoria" -> new ProductoDaoMemoria(); "jdbc" -> new ProductoDaoJdbc(...);
        //         cualquier otro valor -> IllegalArgumentException con un mensaje claro
        throw new UnsupportedOperationException("TODO 9");
    }
}
