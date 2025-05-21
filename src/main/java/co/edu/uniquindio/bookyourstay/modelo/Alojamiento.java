package co.edu.uniquindio.bookyourstay.modelo;

import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Alojamiento {
    private String id;
    private String nombre;
    private String ciudad;
    private String descripcion;
    private float precioNoche;
    private int capacidadMax;
    private List<String> servicios;
    private List<Reserva> reservas = new ArrayList<>();
    private List<Reseña> reseñas = new ArrayList<>();
    private float calificacionPromedio;
    private boolean disponible = true;

    /**
     * Método abstracto para calcular el costo total de la estadía
     * @param numNoches Número de noches de la estadía
     * @return Costo total considerando características específicas del alojamiento
     */
    public abstract float calcularCostoTotal(int numNoches);

    /**
     * Calcula el costo base sin adicionales
     * @param numNoches Número de noches
     * @return Precio por noche multiplicado por el número de noches
     */
    protected float calcularCostoBase(int numNoches) {
        return precioNoche * numNoches;
    }

    /**
     * Verifica la disponibilidad para un rango de fechas
     * @param fechaInicio Fecha de inicio deseada
     * @param fechaFin Fecha de fin deseada
     * @return true si está disponible para esas fechas
     */
    public boolean estaDisponible(LocalDate fechaInicio, LocalDate fechaFin) {
        if (!disponible) return false;

        return reservas.stream()
                .noneMatch(reserva -> reserva.estaActiva() &&
                        reserva.haySolapamiento(fechaInicio, fechaFin));
    }

    /**
     * Agrega una nueva reserva al alojamiento
     * @param reserva Reserva a agregar
     * @throws IllegalStateException Si el alojamiento no está disponible
     */
    public void agregarReserva(Reserva reserva) {
        if (!estaDisponible(reserva.getFechaInicio(), reserva.getFechaFin())) {
            throw new IllegalStateException("El alojamiento no está disponible para las fechas solicitadas");
        }
        reservas.add(reserva);
    }

    /**
     * Agrega una reseña y actualiza la calificación promedio
     * @param reseña Reseña a agregar
     * @throws IllegalArgumentException Si la calificación no está entre 1 y 5
     */
    public void agregarReseña(Reseña reseña) {
        if (reseña.getCalificacion() < 1 || reseña.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }

        reseñas.add(reseña);
        actualizarCalificacionPromedio();
    }
    public String getId() {
        return this.id; // Asegúrate de tener este campo
    }
    /**
     * Actualiza la calificación promedio basada en todas las reseñas
     */
    private void actualizarCalificacionPromedio() {
        if (reseñas.isEmpty()) {
            calificacionPromedio = 0;
            return;
        }

        calificacionPromedio = (float) reseñas.stream()
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0);
    }

    /**
     * Verifica si el alojamiento tiene un servicio específico
     * @param servicio Nombre del servicio a verificar
     * @return true si el alojamiento ofrece ese servicio
     */
    public boolean tieneServicio(String servicio) {
        return servicios.contains(servicio);
    }

    /**
     * Obtiene las reservas activas (confirmadas y pendientes)
     * @return Lista de reservas activas
     */
    public List<Reserva> getReservasActivas() {
        return reservas.stream()
                .filter(Reserva::estaActiva)
                .toList();
    }

    /**
     * Genera un ID único para el alojamiento
     */
    protected String generarId() {
        if (id == null) {
            id = "ALO-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return id;
    }

    /**
     * Valida los datos básicos del alojamiento
     * @throws IllegalArgumentException Si algún dato no es válido
     */
    public void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del alojamiento es requerido");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad del alojamiento es requerida");
        }
        if (precioNoche <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor a cero");
        }
        if (capacidadMax <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a cero");
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) $%,.2f/noche",
                nombre, ciudad, getClass().getSimpleName(), precioNoche);
    }
}