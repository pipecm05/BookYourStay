package co.edu.uniquindio.bookyourstay.modelo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoHabitacion {
    INDIVIDUAL("Habitación Individual", 1),
    DOBLE("Habitación Doble", 2),
    SUITE("Suite", 2),
    FAMILIAR("Habitación Familiar", 4),
    EJECUTIVA("Habitación Ejecutiva", 2);

    private final String descripcion;
    private final int capacidadBase;
}