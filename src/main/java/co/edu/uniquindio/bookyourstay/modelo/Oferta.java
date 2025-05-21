package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoOferta;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoOferta;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oferta {
    private String id;
    private String nombre;
    private String descripcion;
    private TipoOferta tipoOferta;
    private float valor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activa;
    private List<Alojamiento> alojamientosAplicables;
    private int maximoUsos;
    private int usosActuales;
    private String codigoPromocional;
    private EstadoOferta estado;

    /**
     * Constructor para crear nuevas ofertas
     */
    public Oferta(String nombre, String descripcion, TipoOferta tipo, float valor,
                  LocalDate inicio, LocalDate fin, List<Alojamiento> alojamientos, int maxUsos) {
        this.id = "OF-" + UUID.randomUUID().toString().substring(0, 8);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoOferta = tipo;
        this.valor = valor;
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.activa = true;
        this.alojamientosAplicables = alojamientos != null ? new ArrayList<>(alojamientos) : new ArrayList<>();
        this.maximoUsos = maxUsos;
        this.usosActuales = 0;
        this.codigoPromocional = generarCodigoPromocional();
    }

    /**
     * Verifica si la oferta está vigente
     */
    public boolean esVigente() {
        LocalDate hoy = LocalDate.now();
        return activa &&
                !hoy.isBefore(fechaInicio) &&
                !hoy.isAfter(fechaFin) &&
                (maximoUsos == 0 || usosActuales < maximoUsos);
    }

    /**
     * Aplica el descuento al precio original
     */
    public float aplicarDescuento(float precioOriginal) throws IllegalStateException {
        if (!esVigente()) {
            throw new IllegalStateException("La oferta no está vigente");
        }

        float precioConDescuento = switch (tipoOferta) {
            case PORCENTAJE -> precioOriginal * (1 - (valor / 100));
            case MONTO_FIJO -> Math.max(0, precioOriginal - valor);
        };

        usosActuales++;
        return precioConDescuento;
    }

    /**
     * Verifica si la oferta aplica a un alojamiento específico
     */
    public boolean aplicaAAlojamiento(Alojamiento alojamiento) {
        return alojamientosAplicables.isEmpty() ||
                alojamientosAplicables.contains(alojamiento);
    }

    /**
     * Activa o desactiva la oferta
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
        if (!activa) {
            this.usosActuales = maximoUsos; // Marcar como agotada
        }
    }

    /**
     * Obtiene el porcentaje de usos restantes
     */
    public float getPorcentajeDisponible() {
        if (maximoUsos == 0) return 100;
        return 100 - ((float) usosActuales / maximoUsos * 100);
    }

    /**
     * Genera un reporte detallado de la oferta
     */
    public String generarReporte() {
        return String.format(
                "Oferta: %s\n" +
                        "ID: %s\n" +
                        "Tipo: %s\n" +
                        "Valor: %s\n" +
                        "Vigencia: %s a %s\n" +
                        "Estado: %s\n" +
                        "Alojamientos aplicables: %d\n" +
                        "Usos: %d/%d (%.1f%% disponible)\n" +
                        "Código promocional: %s\n" +
                        "Descripción: %s",
                nombre,
                id,
                tipoOferta,
                tipoOferta == TipoOferta.PORCENTAJE ? valor + "%" : "$" + valor,
                fechaInicio,
                fechaFin,
                esVigente() ? "Vigente" : "No vigente",
                alojamientosAplicables.size(),
                usosActuales,
                maximoUsos,
                getPorcentajeDisponible(),
                codigoPromocional,
                descripcion
        );
    }

    // Métodos privados
    private String generarCodigoPromocional() {
        return "PROMO-" + nombre.substring(0, Math.min(3, nombre.length())).toUpperCase() +
                "-" + id.substring(3, 7);
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s)", nombre, tipoOferta, esVigente() ? "Vigente" : "Expirada");
    }


    public boolean getEstado() {
        return this.estado == EstadoOferta.ACTIVA;  // Ejemplo: devuelve true si está activa
    }
}