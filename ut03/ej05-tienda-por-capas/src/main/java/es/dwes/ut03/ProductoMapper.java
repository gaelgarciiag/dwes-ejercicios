package es.dwes.ut03;

import java.math.BigDecimal;

/** Convierte la entidad {@link Producto} en el {@link ProductoDto} que se entrega a la pantalla. */
public final class ProductoMapper {

    private ProductoMapper() { }

    /**
     * Crea el DTO a partir de la entidad.
     *
     * @param tipoIva tipo de IVA como fracción (por ejemplo {@code 0.21} para el 21 %)
     */
    public static ProductoDto aDto(Producto producto, BigDecimal tipoIva) {
        // TODO 7: nombre igual; precioConIva = precio x (1 + tipoIva), con 2 decimales y
        //         RoundingMode.HALF_UP (usa BigDecimal.setScale)
        throw new UnsupportedOperationException("TODO 7");
    }
}
