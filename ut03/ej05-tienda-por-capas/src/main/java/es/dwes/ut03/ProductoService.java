package es.dwes.ut03;

import java.math.BigDecimal;
import java.util.List;

/**
 * Lógica de negocio de la tienda. Trabaja con un {@link ProductoDao} (la interfaz): no sabe
 * si los datos están en MySQL o en memoria, y por eso se puede probar sin base de datos.
 */
public class ProductoService {

    /** IVA general: 21 %. */
    static final BigDecimal IVA = new BigDecimal("0.21");

    private final ProductoDao dao;

    public ProductoService(ProductoDao dao) {
        this.dao = dao;
    }

    /** Todo el catálogo, como DTO con el precio con IVA. */
    public List<ProductoDto> catalogo() {
        // TODO 10: dao.listar() -> ProductoMapper.aDto(producto, IVA)
        throw new UnsupportedOperationException("TODO 10");
    }

    /**
     * Un producto como DTO.
     *
     * @throws ReglaDeNegocioException si no existe
     */
    public ProductoDto obtener(int id) {
        // TODO 11
        throw new UnsupportedOperationException("TODO 11");
    }

    /**
     * Da de alta un producto y devuelve su id.
     *
     * <p>Reglas: el nombre no puede ser nulo ni estar en blanco (se guarda sin espacios al
     * principio ni al final); el precio debe ser mayor que cero; no puede haber otro producto con
     * el mismo nombre (sin distinguir mayúsculas).
     * El mensaje de ese último caso es {@code "Ya existe un producto llamado " + nombre} (con el
     * nombre ya sin espacios sobrantes): {@code App} lo imprime.
     *
     * @throws ReglaDeNegocioException si se incumple alguna regla (y entonces no se guarda nada)
     */
    public int darDeAlta(String nombre, BigDecimal precio) {
        // TODO 12
        throw new UnsupportedOperationException("TODO 12");
    }

    /**
     * Rebaja el precio de un producto un porcentaje entero entre 1 y 90, ambos incluidos.
     * El nuevo precio tiene 2 decimales (HALF_UP). Ejemplo: 24.90 con un 10 % queda en 22.41
     * (24.90 × 0.90 = 22.410) y 12.50 con un 15 % queda en 10.63 (12.50 × 0.85 = 10.625).
     *
     * @throws ReglaDeNegocioException si el porcentaje está fuera de rango o el producto no existe
     */
    public void aplicarDescuento(int id, int porcentaje) {
        // TODO 13
        throw new UnsupportedOperationException("TODO 13");
    }
}
