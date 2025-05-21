package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReservaRepositorio {
    private final List<Reserva> reservas = new ArrayList<>();

    /**
     * Guarda una reserva en el repositorio
     * @param reserva La reserva a guardar
     * @throws IllegalArgumentException si la reserva es nula o ya existe
     */
    public void guardarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        if (existeReserva(reserva.getId())) {
            throw new IllegalArgumentException("Ya existe una reserva con el ID: " + reserva.getId());
        }
        reservas.add(reserva);
    }

    /**
     * Busca una reserva por su ID
     * @param id El ID de la reserva
     * @return Optional con la reserva si existe
     */
    public Optional<Reserva> buscarPorId(String id) {
        return reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    /**
     * Busca reservas por ID de cliente
     * @param clienteId ID del cliente
     * @return Lista de reservas del cliente
     */
    public List<Reserva> buscarPorCliente(String clienteId) {
        return reservas.stream()
                .filter(r -> r.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reservas
     * @return Lista inmutable de todas las reservas
     */
    public List<Reserva> listarTodas() {
        return new ArrayList<>(reservas);
    }

    /**
     * Busca reservas por estado
     * @param estado Estado de la reserva
     * @return Lista de reservas con el estado especificado
     */
    public List<Reserva> buscarPorEstado(EstadoReserva estado) {
        return reservas.stream()
                .filter(r -> r.getEstado() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Busca reservas por alojamiento
     * @param alojamientoId ID del alojamiento
     * @return Lista de reservas para el alojamiento
     */
    public List<Reserva> buscarPorAlojamiento(String alojamientoId) {
        return reservas.stream()
                .filter(r -> r.getAlojamiento().getId().equals(alojamientoId))
                .collect(Collectors.toList());
    }

    /**
     * Busca reservas activas (no canceladas ni completadas)
     * @return Lista de reservas activas
     */
    public List<Reserva> buscarReservasActivas() {
        return reservas.stream()
                .filter(Reserva::estaActiva)
                .collect(Collectors.toList());
    }

    /**
     * Busca reservas en un rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de reservas en el rango
     */
    public List<Reserva> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return reservas.stream()
                .filter(r -> !r.getFechaInicio().isAfter(fechaFin))
                .filter(r -> !r.getFechaFin().isBefore(fechaInicio))
                .collect(Collectors.toList());
    }

    /**
     * Elimina una reserva
     * @param id ID de la reserva a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarReserva(String id) {
        return reservas.removeIf(r -> r.getId().equals(id));
    }

    /**
     * Actualiza una reserva existente
     * @param reservaActualizada Reserva con datos actualizados
     * @throws IllegalArgumentException si la reserva no existe
     */
    public void actualizarReserva(Reserva reservaActualizada) {
        Reserva existente = buscarPorId(reservaActualizada.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        int index = reservas.indexOf(existente);
        reservas.set(index, reservaActualizada);
    }

    /**
     * Verifica si existe una reserva con el ID
     * @param id ID a verificar
     * @return true si existe, false si no
     */
    public boolean existeReserva(String id) {
        return reservas.stream()
                .anyMatch(r -> r.getId().equals(id));
    }

    /**
     * Busca reservas usando un criterio personalizado
     * @param criterio Predicado para filtrar
     * @return Lista de reservas que cumplen el criterio
     */
    public List<Reserva> buscarPorCriterio(Predicate<Reserva> criterio) {
        return reservas.stream()
                .filter(criterio)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta el total de reservas
     * @return Número total de reservas
     */
    public long contarReservas() {
        return reservas.size();
    }

    /**
     * Obtiene reservas próximas a vencer (dentro de 3 días)
     * @return Lista de reservas próximas
     */
    public List<Reserva> buscarProximasAVencer() {
        LocalDate hoy = LocalDate.now();
        return reservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA)
                .filter(r -> r.getFechaFin().isBefore(hoy.plusDays(3)))
                .collect(Collectors.toList());
    }

}