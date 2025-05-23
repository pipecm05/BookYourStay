package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoHabitacion;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Hotel extends Alojamiento {
    private List<Habitacion> habitaciones;
    private int estrellas;
    private boolean tieneRestaurante;
    private boolean tieneBar;
    private boolean tieneGimnasio;
    private boolean tieneSpa;
    private String horarioCheckIn;
    private String horarioCheckOut;
    private List<String> normasInternas;

    public Hotel() {
        this.habitaciones = new ArrayList<>();
        this.normasInternas = new ArrayList<>();
    }

    @Override
    public float calcularCostoTotal(int numNoches) {
        validarNumeroNoches(numNoches);
        return getPrecioNoche().get() * numNoches;
    }
    public Hotel(String nombre, String ciudad, String descripcion,
                 int capacidadMax, float precioNoche) {
        super(
                nombre,
                ciudad,
                UsuarioActual.getInstancia().getUsuario(), // Propietario
                descripcion,
                TipoAlojamiento.HOTEL, // Tipo específico
                precioNoche,
                capacidadMax
        );
    }
    /**
     * Agrega una nueva habitación al hotel
     */
    public void agregarHabitacion(Habitacion habitacion) {
        if (existeHabitacion(habitacion.getNumero())) {
            throw new IllegalArgumentException("Ya existe una habitación con ese número");
        }
        habitaciones.add(habitacion);
    }

    /**
     * Busca una habitación por su número
     */
    public Habitacion buscarHabitacion(String numero) {
        return habitaciones.stream()
                .filter(h -> h.getNumero().equals(numero))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene habitaciones disponibles para un rango de fechas
     */
    public List<Habitacion> obtenerHabitacionesDisponibles(LocalDate fechaInicio, LocalDate fechaFin, int personas) {
        return habitaciones.stream()
                .filter(h -> h.estaDisponible(fechaInicio, fechaFin))
                .filter(h -> h.getCapacidad() >= personas)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene habitaciones por tipo
     */
    public List<Habitacion> obtenerHabitacionesPorTipo(TipoHabitacion tipo) {
        return habitaciones.stream()
                .filter(h -> h.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el precio promedio de las habitaciones
     */
    public float calcularPrecioPromedio() {
        if (habitaciones.isEmpty()) return 0;

        return (float) habitaciones.stream()
                .mapToDouble(Habitacion::getPrecio)
                .average()
                .orElse(0);
    }

    /**
     * Obtiene los tipos de habitación disponibles
     */
    public List<TipoHabitacion> obtenerTiposHabitacionDisponibles() {
        return habitaciones.stream()
                .map(Habitacion::getTipo)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Verifica si existe una habitación con el número dado
     */
    public boolean existeHabitacion(String numero) {
        return habitaciones.stream()
                .anyMatch(h -> h.getNumero().equals(numero));
    }

    /**
     * Genera un reporte detallado del hotel
     */
    public String generarReporte() {
        return String.format(
                "Hotel: %s\n" +
                        "Ubicación: %s\n" +
                        "Estrellas: %d\n" +
                        "Habitaciones: %d\n" +
                        "Precio promedio: $%,.2f\n" +
                        "Servicios:\n" +
                        "- Restaurante: %s\n" +
                        "- Bar: %s\n" +
                        "- Gimnasio: %s\n" +
                        "- Spa: %s\n" +
                        "Horarios:\n" +
                        "- Check-in: %s\n" +
                        "- Check-out: %s\n" +
                        "Normas internas:\n%s",
                getNombre(),
                getCiudad(),
                estrellas,
                habitaciones.size(),
                calcularPrecioPromedio(),
                tieneRestaurante ? "Sí" : "No",
                tieneBar ? "Sí" : "No",
                tieneGimnasio ? "Sí" : "No",
                tieneSpa ? "Sí" : "No",
                horarioCheckIn,
                horarioCheckOut,
                String.join("\n- ", normasInternas)
        );
    }

    @Override
    public void validar() {
        super.validar();

        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException("El número de estrellas debe estar entre 1 y 5");
        }
        if (horarioCheckIn == null || horarioCheckIn.isBlank()) {
            throw new IllegalArgumentException("El horario de check-in es requerido");
        }
        if (horarioCheckOut == null || horarioCheckOut.isBlank()) {
            throw new IllegalArgumentException("El horario de check-out es requerido");
        }
    }

    private void validarNumeroNoches(int numNoches) {
        if (numNoches <= 0) {
            throw new IllegalArgumentException("El número de noches debe ser positivo");
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d estrellas) %d habitaciones",
                getNombre(), getCiudad(), estrellas, habitaciones.size());
    }
}
