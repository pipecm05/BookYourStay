package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.UUID;

public class Reserva {
    private final String id;
    private final Cliente cliente;
    private final Alojamiento alojamiento;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final int numHuespedes;
    private double total;
    private EstadoReserva estado;
    private Factura factura;  // Asegúrate de tener este atributo

    public Reserva(Cliente cliente, Alojamiento alojamiento,
                   LocalDate fechaInicio, LocalDate fechaFin,
                   int numHuespedes) {
        this.id = UUID.randomUUID().toString();
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numHuespedes = numHuespedes;
        this.estado = EstadoReserva.PENDIENTE;
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        int numNoches = fechaInicio.until(fechaFin).getDays();
        return alojamiento.calcularCostoTotal(numNoches);
    }

    // Métodos getters
    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Alojamiento getAlojamiento() { return alojamiento; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getNumHuespedes() { return numHuespedes; }
    public double getTotal() { return total; }
    public EstadoReserva getEstado() { return estado; }
    public Factura getFactura() { return factura; }

    // Métodos setters necesarios
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public void setTotal(double total) { this.total = total; }
    public void setFactura(Factura factura) { this.factura = factura; }  // Este es el método que faltaba

    // Método para cancelar
    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }
}