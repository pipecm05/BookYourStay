package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.repositorios.ReservaRepositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ReservaServicio {
    private List<Reserva> todasLasReservas;
    private final ReservaRepositorio reservaRepositorio;
    private static ReservaServicio instancia;

    public ReservaServicio(ReservaRepositorio reservaRepositorio) {
        this.reservaRepositorio = reservaRepositorio;
    }
    public static synchronized ReservaServicio obtenerInstancia() {
        if (instancia == null) {
            instancia = new ReservaServicio(new ReservaRepositorio());
        }
        return instancia;
    }
    public Reserva crearReserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio,
                                LocalDate fechaFin, int numHuespedes) throws IllegalArgumentException, IllegalStateException {

        validarFechas(fechaInicio, fechaFin);
        validarCapacidad(alojamiento, numHuespedes);
        validarDisponibilidad(alojamiento, fechaInicio, fechaFin);
        validarSaldoCliente(cliente, alojamiento, fechaInicio, fechaFin);

        Reserva reserva = construirReserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes);
        reservaRepositorio.guardarReserva(reserva);
        return reserva;
    }

    public Reserva cancelarReserva(String reservaId, String motivo) throws NoSuchElementException, IllegalStateException {
        Reserva reserva = obtenerReserva(reservaId);
        validarCancelacion(reserva);

        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setNotasEspeciales(motivo);
        reservaRepositorio.actualizarReserva(reserva);
        return reserva;
    }

    public Reserva obtenerReserva(String reservaId) throws NoSuchElementException {
        return reservaRepositorio.buscarPorId(reservaId)
                .orElseThrow(() -> new NoSuchElementException("Reserva no encontrada con ID: " + reservaId));
    }

    public Factura generarFactura(String reservaId, String metodoPago) throws NoSuchElementException {
        try {
            Reserva reserva = obtenerReserva(reservaId);

            if (metodoPago == null || metodoPago.isBlank()) {
                throw new IllegalArgumentException("El método de pago no puede estar vacío");
            }

            return Factura.generarFactura(reserva, metodoPago);

        } catch (NoSuchElementException e) {
            throw e; // Relanzamos la excepción de reserva no encontrada
        } catch (Exception e) {
            throw new RuntimeException("Error al generar factura: " + e.getMessage(), e);
        }
    }
    public List<Reserva> listarReservasActivasCliente(String clienteId) {
        return reservaRepositorio.buscarPorCliente(clienteId).stream()
                .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA)
                .collect(Collectors.toList());
    }

    public boolean verificarDisponibilidad(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin) {
        return reservaRepositorio.buscarPorAlojamiento(alojamiento.getId()).stream()
                .noneMatch(r -> r.haySolapamiento(fechaInicio, fechaFin) && r.getEstado() == EstadoReserva.CONFIRMADA);
    }

    public Reserva completarReserva(String reservaId) throws NoSuchElementException {
        Reserva reserva = obtenerReserva(reservaId);
        reserva.setEstado(EstadoReserva.COMPLETADA);
        reservaRepositorio.actualizarReserva(reserva);
        return reserva;
    }

    private Reserva construirReserva(Cliente cliente, Alojamiento alojamiento,
                                     LocalDate fechaInicio, LocalDate fechaFin, int numHuespedes) {
        int numNoches = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        float total = alojamiento.calcularCostoTotal(numNoches);

        return Reserva.builder()
                .conId(generarId())  // Cambiado a conId
                .conCliente(cliente)
                .conAlojamiento(alojamiento)
                .conFechaInicio(fechaInicio)
                .conFechaFin(fechaFin)
                .conNumHuespedes(numHuespedes)
                .conEstado(EstadoReserva.CONFIRMADA)
                .conTotal(total)
                .build();
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) throws IllegalArgumentException {
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("Las fechas de reserva no son válidas");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado");
        }
    }

    private void validarCapacidad(Alojamiento alojamiento, int numHuespedes) throws IllegalArgumentException {
        if (numHuespedes > alojamiento.getCapacidadMax().get()) {
            throw new IllegalArgumentException("Capacidad máxima excedida: " + alojamiento.getCapacidadMax().get());
        }
    }

    private void validarDisponibilidad(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin)
            throws IllegalStateException {
        if (!verificarDisponibilidad(alojamiento, fechaInicio, fechaFin)) {
            throw new IllegalStateException("El alojamiento no está disponible para las fechas seleccionadas");
        }
    }

    private void validarSaldoCliente(Cliente cliente, Alojamiento alojamiento,
                                     LocalDate fechaInicio, LocalDate fechaFin) throws IllegalStateException {
        int numNoches = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        float total = alojamiento.calcularCostoTotal(numNoches);

        if (!cliente.tieneSaldoSuficiente(total)) {
            throw new IllegalStateException("Saldo insuficiente. Se requiere: " + total);
        }
    }

    private void validarCancelacion(Reserva reserva) throws IllegalStateException {
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se pueden cancelar reservas confirmadas");
        }
        if (reserva.getFechaInicio().minusDays(2).isBefore(LocalDate.now())) {
            throw new IllegalStateException("Solo se pueden cancelar con más de 48 horas de anticipación");
        }
    }
    public boolean estaDisponible(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin) {
        if (alojamiento == null || fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Parámetros no pueden ser nulos");
        }

        return todasLasReservas.stream()
                .filter(r -> r.getAlojamiento() != null &&
                        r.getAlojamiento().getId().equals(alojamiento.getId()))
                .noneMatch(reserva -> reserva.haySolapamiento(fechaInicio, fechaFin));
    }
    private String generarId() {
        return "RES-" + System.currentTimeMillis();
    }
}