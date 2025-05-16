package co.edu.uniquindio.bookyourstay.modelo;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends Alojamiento {
    private List<Habitacion> habitaciones;

    public Hotel(String nombre, String ciudad, String descripcion,
                 String imagen, double precioNoche, int capacidad) {
        super(nombre, ciudad, descripcion, imagen, precioNoche, capacidad);
        this.habitaciones = new ArrayList<>();
    }

    @Override
    public double calcularCostoTotal(int numNoches) {
        return getPrecioNoche() * numNoches;
    }

    public void agregarHabitacion(Habitacion habitacion) {
        habitaciones.add(habitacion);
    }

    // Getters
    public List<Habitacion> getHabitaciones() { return habitaciones; }
}