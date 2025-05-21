package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoHabitacion;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Habitacion {
    private String numero;
    private float precio;
    private int capacidad;
    private String descripcion;
    private String imagenUrl;
    private List<String> servicios;
    private TipoHabitacion tipo;
    private boolean disponible;
    private List<Reserva> reservas;

    public Habitacion() {
        this.servicios = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.disponible = true;
    }

    /**
     * Verifica la disponibilidad para un rango de fechas
     */
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        if (!disponible) return false;

        return reservas.stream().noneMatch(reserva ->
                reserva.estaActiva() && reserva.haySolapamiento(fechaInicio, fechaFin)
        );
    }

    /**
     * Agrega una reserva a la habitación
     */
    public void agregarReserva(Reserva reserva) {
        if (!estaDisponible(reserva.getFechaInicio(), reserva.getFechaFin())) {
            throw new IllegalStateException("La habitación no está disponible para esas fechas");
        }
        reservas.add(reserva);
    }

    /**
     * Calcula el costo total para una estadía
     */
    public float calcularCostoTotal(int numNoches) {
        if (numNoches <= 0) {
            throw new IllegalArgumentException("El número de noches debe ser positivo");
        }
        return precio * numNoches;
    }

    /**
     * Obtiene las reservas activas
     */
    public List<Reserva> getReservasActivas() {
        return reservas.stream()
                .filter(Reserva::estaActiva)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si tiene un servicio específico
     */
    public boolean tieneServicio(String servicio) {
        return servicios.contains(servicio);
    }

    /**
     * Obtiene información detallada de la habitación
     */
    public String obtenerInformacionDetallada() {
        return String.format(
                "Habitación %s\n" +
                        "Tipo: %s\n" +
                        "Capacidad: %d personas\n" +
                        "Precio por noche: $%,.2f\n" +
                        "Servicios: %s\n" +
                        "Descripción: %s",
                numero,
                tipo.getDescripcion(),
                capacidad,
                precio,
                String.join(", ", servicios),
                descripcion
        );
    }

    @Override
    public String toString() {
        return String.format("Habitación %s - %s (%d pers.) $%,.2f/noche",
                numero, tipo, capacidad, precio);
    }
}