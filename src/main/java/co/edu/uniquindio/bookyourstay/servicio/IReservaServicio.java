package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservaServicio {
    Reserva crearReserva(Cliente cliente, Alojamiento alojamiento,
                         LocalDate fechaInicio, LocalDate fechaFin,
                         int numHuespedes) throws Exception;
    boolean cancelarReserva(String idReserva) throws Exception;
    Optional<Reserva> buscarReserva(String id);
    List<Reserva> listarReservasPorCliente(String idCliente);
    List<Reserva> listarReservasPorAlojamiento(String idAlojamiento);
    boolean verificarDisponibilidad(String idAlojamiento,
                                    LocalDate fechaInicio,
                                    LocalDate fechaFin);
    Factura generarFactura(Reserva reserva);
    boolean agregarResena(String idReserva, String comentario, int calificacion) throws Exception;
}