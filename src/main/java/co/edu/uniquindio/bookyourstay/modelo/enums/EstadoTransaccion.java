package co.edu.uniquindio.bookyourstay.modelo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoTransaccion {
    PENDIENTE("⏳", "Pendiente"),
    COMPLETADA("✅", "Completada"),
    FALLIDA("❌", "Fallida"),
    REVERSADA("↩️", "Reversada");

    private final String icono;
    private final String descripcion;
}