package co.edu.uniquindio.bookyourstay.modelo;

import java.time.LocalDate;

public class Oferta {
    private Alojamiento alojamiento;  // Relación con Alojamiento
    private String descripcion;
    private double descuento;  // Valor entre 0 y 1 (ej. 0.2 = 20%)
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Oferta(Alojamiento alojamiento, String descripcion,
                  double descuento, LocalDate fechaInicio,
                  LocalDate fechaFin) {
        this.alojamiento = alojamiento;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        // Validación para asegurar que el descuento está entre 0 y 1
        if (descuento < 0 || descuento > 1) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 1");
        }
        this.descuento = descuento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        // Validación para asegurar que fechaFin es posterior a fechaInicio
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior a la fecha de inicio");
        }
        this.fechaFin = fechaFin;
    }
}