package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Reserva {
    private String id;
    private Cliente cliente;
    private Alojamiento alojamiento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int numHuespedes;
    private EstadoReserva estado;
    private float subtotal;
    private float descuentos;
    private float impuestos;
    private float total;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Factura factura;
    private List<Huesped> huespedes;
    private String notasEspeciales;
    private String metodoPago;
    private String codigoConfirmacion;

    public Reserva() {
        this.id = "RES-" + UUID.randomUUID().toString().substring(0, 8);
        this.estado = EstadoReserva.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.huespedes = new ArrayList<>();

    }

    /**
     * Calcula el número de noches de la reserva
     */
    public int getNumeroNoches() {
        return (int) fechaInicio.until(fechaFin).getDays();
    }
    public Alojamiento getAlojamiento() {
        return this.alojamiento;
    }
    /**
     * Verifica si la reserva está activa (confirmada)
     */
    public boolean estaActiva() {
        return estado == EstadoReserva.CONFIRMADA;
    }

    /**
     * Verifica si hay solapamiento con otro rango de fechas
     */
    public boolean haySolapamiento(LocalDate otraInicio, LocalDate otraFin) {
        return !fechaFin.isBefore(otraInicio) && !fechaInicio.isAfter(otraFin);
    }

    /**
     * Confirma la reserva
     */
    public void confirmar(String metodoPago) {
        if (estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar reservas pendientes");
        }
        this.estado = EstadoReserva.CONFIRMADA;
        this.metodoPago = metodoPago;
        this.codigoConfirmacion = generarCodigoConfirmacion();
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Cancela la reserva
     */
    public void cancelar(String motivo) {
        if (estado != EstadoReserva.CONFIRMADA && estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("No se puede cancelar una reserva en estado " + estado);
        }
        this.estado = EstadoReserva.CANCELADA;
        this.notasEspeciales = (notasEspeciales == null ? "" : notasEspeciales + "\n") +
                "Cancelación: " + motivo;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Marca la reserva como completada
     */
    public void completar() {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se pueden completar reservas confirmadas");
        }
        this.estado = EstadoReserva.COMPLETADA;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Agrega un huésped a la reserva
     */
    public void agregarHuesped(Huesped huesped) {
        if (huespedes.size() >= numHuespedes) {
            throw new IllegalStateException("No se pueden agregar más huéspedes que el número especificado");
        }
        huespedes.add(huesped);
    }

    /**
     * Calcula el subtotal basado en el precio del alojamiento
     */
    public void calcularSubtotal() {
        if (alojamiento == null) {
            throw new IllegalStateException("No hay alojamiento asociado a la reserva");
        }
        this.subtotal = alojamiento.calcularCostoBase(getNumeroNoches());
    }

    /**
     * Aplica descuentos a la reserva
     */
    public void aplicarDescuentos(float descuento) {
        this.descuentos = descuento;
        calcularTotal();
    }

    /**
     * Calcula el total incluyendo impuestos
     */
    private void calcularTotal() {
        this.impuestos = subtotal * 0.19f; // IVA colombiano 19%
        this.total = subtotal - descuentos + impuestos;
    }

    /**
     * Valida que la reserva sea correcta
     */
    public void validar() {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        if (numHuespedes <= 0) {
            throw new IllegalArgumentException("El número de huéspedes debe ser positivo");
        }
        if (alojamiento != null && numHuespedes > alojamiento.getCapacidadMax()) {
            throw new IllegalArgumentException("El número de huéspedes excede la capacidad del alojamiento");
        }
    }

    /**
     * Genera un resumen de la reserva
     */
    public String generarResumen() {
        return String.format(
                "Reserva #%s\n" +
                        "Alojamiento: %s\n" +
                        "Fecha: %s a %s (%d noches)\n" +
                        "Huéspedes: %d\n" +
                        "Estado: %s\n" +
                        "Subtotal: $%,.2f\n" +
                        "Descuentos: $%,.2f\n" +
                        "Impuestos: $%,.2f\n" +
                        "Total: $%,.2f\n" +
                        "Código: %s",
                id,
                alojamiento != null ? alojamiento.getNombre() : "No asignado",
                fechaInicio,
                fechaFin,
                getNumeroNoches(),
                numHuespedes,
                estado,
                subtotal,
                descuentos,
                impuestos,
                total,
                codigoConfirmacion != null ? codigoConfirmacion : "Pendiente"
        );
    }

    // Métodos privados
    private String generarCodigoConfirmacion() {
        return "CONF-" + id.substring(4, 8) + "-" +
                fechaInicio.getDayOfMonth() +
                fechaInicio.getMonthValue();
    }

    @Override
    public String toString() {
        return String.format("Reserva #%s - %s (%s)", id,
                alojamiento != null ? alojamiento.getNombre() : "Sin alojamiento",
                estado);
    }
}