package co.edu.uniquindio.bookyourstay.modelo.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum EstadoOferta {
    ACTIVA("Activa", "🟢", "oferta-activa"),
    PAUSADA("Pausada", "⏸️", "oferta-pausada"),
    AGOTADA("Agotada", "🔴", "oferta-agotada"),
    PROGRAMADA("Programada", "📅", "oferta-programada"),
    EXPIRADA("Expirada", "⌛", "oferta-expirada"),
    RECHAZADA("Rechazada", "❌", "oferta-rechazada");

    private final String descripcion;
    private final String icono;
    private final String claseCss;

    EstadoOferta(String descripcion, String icono, String claseCss) {
        this.descripcion = descripcion;
        this.icono = icono;
        this.claseCss = claseCss;
    }

    /**
     * Verifica si la oferta puede ser aplicada a reservas
     */
    public boolean esAplicable() {
        return this == ACTIVA;
    }

    /**
     * Verifica si la oferta puede ser editada
     */
    public boolean permiteEdicion() {
        return this == ACTIVA || this == PAUSADA || this == PROGRAMADA;
    }

    /**
     * Obtiene los estados visibles para clientes
     */
    public static List<EstadoOferta> estadosVisibles() {
        return List.of(ACTIVA, PROGRAMADA);
    }

    /**
     * Verifica si la oferta es visible en el sistema
     */
    public boolean esVisible() {
        return estadosVisibles().contains(this);
    }

    /**
     * Obtiene los estados que permiten activación
     */
    public static List<EstadoOferta> estadosActivables() {
        return List.of(PAUSADA, PROGRAMADA);
    }

    @Override
    public String toString() {
        return descripcion;
    }
}