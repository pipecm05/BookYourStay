package co.edu.uniquindio.bookyourstay.modelo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoCalificacion {
    ESTANDAR("Reseña estándar"),
    DETALLADA("Reseña detallada"),
    PREMIUM("Reseña premium");

    private final String descripcion;
}