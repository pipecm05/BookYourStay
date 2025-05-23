package co.edu.uniquindio.bookyourstay.modelo.build;

import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Huesped;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservaBuilder {

    private final Reserva reserva;

    public ReservaBuilder() {
        this.reserva = new Reserva();
    }

    // Copiar desde otra reserva (opcional)
    public ReservaBuilder desde(Reserva base) {
        reserva.setId(base.getId());
        reserva.setAlojamiento(base.getAlojamiento());
        reserva.setCliente(base.getCliente());
        reserva.setFechaInicio(base.getFechaInicio());
        reserva.setFechaFin(base.getFechaFin());
        reserva.setEstado(base.getEstado());
        reserva.setMetodoPago(base.getMetodoPago());
        reserva.setCodigoConfirmacion(base.getCodigoConfirmacion());
        reserva.setFechaActualizacion(base.getFechaActualizacion());
        reserva.setNotasEspeciales(base.getNotasEspeciales());
        reserva.setNumHuespedes(base.getNumHuespedes());
        reserva.setHuespedes(FXCollections.observableArrayList(base.getHuespedes())); // evitar referencia directa
        reserva.setSubtotal(base.getSubtotal());
        reserva.setDescuentos(base.getDescuentos());
        reserva.setImpuestos(base.getImpuestos());
        reserva.setTotal(base.getTotal());
        return this;
    }

    public ReservaBuilder conId(String id) {
        reserva.setId(id);
        return this;
    }

    public ReservaBuilder conAlojamiento(Alojamiento alojamiento) {
        reserva.setAlojamiento(alojamiento);
        return this;
    }

    public ReservaBuilder conCliente(Cliente cliente) {
        reserva.setCliente(cliente);
        return this;
    }

    public ReservaBuilder conFechaInicio(LocalDate fechaInicio) {
        reserva.setFechaInicio(fechaInicio);
        return this;
    }

    public ReservaBuilder conFechaFin(LocalDate fechaFin) {
        reserva.setFechaFin(fechaFin);
        return this;
    }

    public ReservaBuilder conEstado(EstadoReserva estado) {
        reserva.setEstado(estado);
        return this;
    }

    public ReservaBuilder conMetodoPago(String metodoPago) {
        reserva.setMetodoPago(metodoPago);
        return this;
    }

    public ReservaBuilder conCodigoConfirmacion(String codigoConfirmacion) {
        reserva.setCodigoConfirmacion(codigoConfirmacion);
        return this;
    }

    public ReservaBuilder conFechaActualizacion(LocalDateTime fechaActualizacion) {
        reserva.setFechaActualizacion(fechaActualizacion);
        return this;
    }

    public ReservaBuilder conNotasEspeciales(String notasEspeciales) {
        reserva.setNotasEspeciales(notasEspeciales);
        return this;
    }

    public ReservaBuilder conNumHuespedes(int numHuespedes) {
        reserva.setNumHuespedes(numHuespedes);
        return this;
    }

    public ReservaBuilder conHuespedes(List<Huesped> huespedes) {
        reserva.setHuespedes(FXCollections.observableArrayList(huespedes));
        return this;
    }

    public ReservaBuilder conSubtotal(float subtotal) {
        reserva.setSubtotal(subtotal);
        return this;
    }

    public ReservaBuilder conDescuentos(float descuentos) {
        reserva.setDescuentos(descuentos);
        return this;
    }

    public ReservaBuilder conImpuestos(float impuestos) {
        reserva.setImpuestos(impuestos);
        return this;
    }

    public ReservaBuilder conTotal(float total) {
        reserva.setTotal(total);
        return this;
    }

    public Reserva build() {
        return reserva;
    }
}