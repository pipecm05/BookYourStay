package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.build.ReservaBuilder;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Reserva {
    private final StringProperty id = new SimpleStringProperty();
    private final ObjectProperty<Alojamiento> alojamiento = new SimpleObjectProperty<>();
    private final ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> fechaInicio = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> fechaFin = new SimpleObjectProperty<>();
    private final ObjectProperty<EstadoReserva> estado = new SimpleObjectProperty<>();
    private final StringProperty metodoPago = new SimpleStringProperty();
    private final StringProperty codigoConfirmacion = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> fechaActualizacion = new SimpleObjectProperty<>();
    private final StringProperty notasEspeciales = new SimpleStringProperty();
    private final IntegerProperty numHuespedes = new SimpleIntegerProperty();
    private final ListProperty<Huesped> huespedes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final FloatProperty subtotal = new SimpleFloatProperty();
    private final FloatProperty descuentos = new SimpleFloatProperty();
    private final FloatProperty impuestos = new SimpleFloatProperty();
    private final FloatProperty total = new SimpleFloatProperty();

    // Getters y Setters para propiedades
    public String getId() { return id.get(); }
    public void setId(String value) { id.set(value); }
    public StringProperty idProperty() { return id; }

    public Alojamiento getAlojamiento() { return alojamiento.get(); }
    public void setAlojamiento(Alojamiento value) { alojamiento.set(value); }
    public ObjectProperty<Alojamiento> alojamientoProperty() { return alojamiento; }

    public Cliente getCliente() { return cliente.get(); }
    public void setCliente(Cliente value) { cliente.set(value); }
    public ObjectProperty<Cliente> clienteProperty() { return cliente; }

    public LocalDate getFechaInicio() { return fechaInicio.get(); }
    public void setFechaInicio(LocalDate value) { fechaInicio.set(value); }
    public ObjectProperty<LocalDate> fechaInicioProperty() { return fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin.get(); }
    public void setFechaFin(LocalDate value) { fechaFin.set(value); }
    public ObjectProperty<LocalDate> fechaFinProperty() { return fechaFin; }

    public EstadoReserva getEstado() { return estado.get(); }
    public void setEstado(EstadoReserva value) { estado.set(value); }
    public ObjectProperty<EstadoReserva> estadoProperty() { return estado; }

    public String getMetodoPago() { return metodoPago.get(); }
    public void setMetodoPago(String value) { metodoPago.set(value); }
    public StringProperty metodoPagoProperty() { return metodoPago; }

    public String getCodigoConfirmacion() { return codigoConfirmacion.get(); }
    public void setCodigoConfirmacion(String value) { codigoConfirmacion.set(value); }
    public StringProperty codigoConfirmacionProperty() { return codigoConfirmacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion.get(); }
    public void setFechaActualizacion(LocalDateTime value) { fechaActualizacion.set(value); }
    public ObjectProperty<LocalDateTime> fechaActualizacionProperty() { return fechaActualizacion; }

    public String getNotasEspeciales() { return notasEspeciales.get(); }
    public void setNotasEspeciales(String value) { notasEspeciales.set(value); }
    public StringProperty notasEspecialesProperty() { return notasEspeciales; }

    public int getNumHuespedes() { return numHuespedes.get(); }
    public void setNumHuespedes(int value) { numHuespedes.set(value); }
    public IntegerProperty numHuespedesProperty() { return numHuespedes; }

    public ObservableList<Huesped> getHuespedes() { return huespedes.get(); }
    public void setHuespedes(ObservableList<Huesped> value) { huespedes.set(value); }
    public ListProperty<Huesped> huespedesProperty() { return huespedes; }

    public float getSubtotal() { return subtotal.get(); }
    public void setSubtotal(float value) { subtotal.set(value); }
    public FloatProperty subtotalProperty() { return subtotal; }

    public float getDescuentos() { return descuentos.get(); }
    public void setDescuentos(float value) { descuentos.set(value); }
    public FloatProperty descuentosProperty() { return descuentos; }

    public float getImpuestos() { return impuestos.get(); }
    public void setImpuestos(float value) { impuestos.set(value); }
    public FloatProperty impuestosProperty() { return impuestos; }

    public float getTotal() { return total.get(); }
    public void setTotal(float value) { total.set(value); }
    public FloatProperty totalProperty() { return total; }

    // Métodos de negocio (se mantienen iguales pero adaptados a las propiedades)
    public boolean haySolapamiento(LocalDate otraInicio, LocalDate otraFin) {
        return !getFechaFin().isBefore(otraInicio) && !getFechaInicio().isAfter(otraFin);
    }

    public boolean estaActiva() {
        return getEstado() == EstadoReserva.CONFIRMADA || getEstado() == EstadoReserva.PENDIENTE;
    }

    public String getCiudadAlojamiento() {
        return getAlojamiento() != null ? getAlojamiento().ciudadProperty().get() : "Sin alojamiento";
    }

    public int getNumeroNoches() {
        return (int) getFechaInicio().until(getFechaFin()).getDays();
    }

    public void confirmar(String metodoPago) {
        if (getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar reservas pendientes");
        }
        setEstado(EstadoReserva.CONFIRMADA);
        setMetodoPago(metodoPago);
        setCodigoConfirmacion(generarCodigoConfirmacion());
        setFechaActualizacion(LocalDateTime.now());
    }

    public void cancelar(String motivo) {
        if (getEstado() != EstadoReserva.CONFIRMADA && getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("No se puede cancelar una reserva en estado " + getEstado());
        }
        setEstado(EstadoReserva.CANCELADA);
        setNotasEspeciales((getNotasEspeciales() == null ? "" : getNotasEspeciales() + "\n") +
                "Cancelación: " + motivo);
        setFechaActualizacion(LocalDateTime.now());
    }

    public void completar() {
        if (getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se pueden completar reservas confirmadas");
        }
        setEstado(EstadoReserva.COMPLETADA);
        setFechaActualizacion(LocalDateTime.now());
    }

    public void agregarHuesped(Huesped huesped) {
        if (getHuespedes().size() >= getNumHuespedes()) {
            throw new IllegalStateException("No se pueden agregar más huéspedes que el número especificado");
        }
        getHuespedes().add(huesped);
    }

    public void calcularSubtotal() {
        if (getAlojamiento() == null) {
            throw new IllegalStateException("No hay alojamiento asociado a la reserva");
        }
        setSubtotal(getAlojamiento().calcularCostoBase(getNumeroNoches()));
    }

    public void aplicarDescuentos(float descuento) {
        setDescuentos(descuento);
        calcularTotal();
    }
    public static ReservaBuilder builder() {
        return new ReservaBuilder();
    }
    private void calcularTotal() {
        setImpuestos(getSubtotal() * 0.19f); // IVA colombiano 19%
        setTotal(getSubtotal() - getDescuentos() + getImpuestos());
    }

    public void validar() {
        if (getFechaInicio() == null || getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (getFechaFin().isBefore(getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        if (getNumHuespedes() <= 0) {
            throw new IllegalArgumentException("El número de huéspedes debe ser positivo");
        }
        if (getAlojamiento() != null && getNumHuespedes() > getAlojamiento().getCapacidadMax().get()) {
            throw new IllegalArgumentException("El número de huéspedes excede la capacidad del alojamiento");
        }
    }

    public String generarResumen() {
        return String.format(
                "Reserva #%s\nAlojamiento: %s\nCiudad: %s\nFecha: %s a %s (%d noches)\n" +
                        "Huéspedes: %d\nEstado: %s\nSubtotal: $%,.2f\nDescuentos: $%,.2f\n" +
                        "Impuestos: $%,.2f\nTotal: $%,.2f\nCódigo: %s",
                getId(),
                getAlojamiento() != null ? getAlojamiento().getNombre() : "No asignado",
                getCiudadAlojamiento(),
                getFechaInicio(),
                getFechaFin(),
                getNumeroNoches(),
                getNumHuespedes(),
                getEstado(),
                getSubtotal(),
                getDescuentos(),
                getImpuestos(),
                getTotal(),
                getCodigoConfirmacion() != null ? getCodigoConfirmacion() : "Pendiente"
        );
    }

    private String generarCodigoConfirmacion() {
        return "CONF-" + getId().substring(4, 8) + "-" +
                getFechaInicio().getDayOfMonth() +
                getFechaInicio().getMonthValue();
    }

    public float calcularPrecioTotal() {
        calcularSubtotal();
        calcularTotal();
        return getTotal();
    }

    @Override
    public String toString() {
        return String.format("Reserva #%s - %s (%s) en %s", getId(),
                getAlojamiento() != null ? getAlojamiento().getNombre() : "Sin alojamiento",
                getEstado(), getCiudadAlojamiento());
    }
}