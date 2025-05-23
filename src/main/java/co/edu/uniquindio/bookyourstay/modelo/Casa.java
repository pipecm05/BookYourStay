package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Casa extends Alojamiento {
    private static final float COSTO_ASEO = 50000; // Costo adicional por aseo
    private static final float DEPOSITO_SEGURIDAD = 150000; // Depósito reembolsable

    // Atributos específicos de casas
    private int numeroHabitaciones;
    private int numeroBanos;
    private float areaTerreno; // m²
    private float areaConstruida; // m²
    private boolean tieneJardin;
    private boolean tienePiscina;
    private boolean permiteEventos;
    private int capacidadParqueadero;
    private String normasExternas;

    @Override
    public float calcularCostoTotal(int numNoches) {
        validarNumeroNoches(numNoches);
        float costoBase = getPrecioNoche().get() * numNoches;
        float costoServicios = calcularCostoServiciosAdicionales();
        return costoBase + COSTO_ASEO + costoServicios + DEPOSITO_SEGURIDAD;
    }
    public Casa(String nombre, String ciudad, String descripcion,
                int capacidadMax, float precioNoche) {
        super(
                nombre,
                ciudad,
                UsuarioActual.getInstancia().getUsuario(), // Propietario (requerido)
                descripcion,
                TipoAlojamiento.CASA, // Tipo fijo para casas
                precioNoche,
                capacidadMax
        );
        // Inicializa atributos por defecto
        this.numeroHabitaciones = 1;
        this.numeroBanos = 1;
        this.areaTerreno = 100; // m²
        this.areaConstruida = 80; // m²
    }
    /**
     * Calcula costos adicionales por servicios especiales
     */
    private float calcularCostoServiciosAdicionales() {
        float adicional = 0;
        if (tienePiscina) {
            adicional += 30000; // Mantenimiento de piscina
        }
        if (permiteEventos) {
            adicional += 70000; // Seguro para eventos
        }
        return adicional;
    }

    /**
     * Genera un reporte detallado de la casa
     */
    public String generarReporte() {
        return String.format(
                "Casa: %s\n" +
                        "Ubicación: %s\n" +
                        "Habitaciones: %d\n" +
                        "Baños: %d\n" +
                        "Área terreno: %.2f m²\n" +
                        "Área construida: %.2f m²\n" +
                        "Jardín: %s\n" +
                        "Piscina: %s\n" +
                        "Permite eventos: %s\n" +
                        "Parqueaderos: %d\n" +
                        "Precio base/noche: $%,.2f\n" +
                        "Costo aseo: $%,.2f\n" +
                        "Depósito seguridad: $%,.2f\n" +
                        "Normas externas: %s\n" +
                        "Servicios incluidos: %s\n" +
                        "Calificación promedio: %.1f/5",
                getNombre(),
                getCiudad(),
                numeroHabitaciones,
                numeroBanos,
                areaTerreno,
                areaConstruida,
                tieneJardin ? "Sí" : "No",
                tienePiscina ? "Sí" : "No",
                permiteEventos ? "Sí" : "No",
                capacidadParqueadero,
                getPrecioNoche(),
                COSTO_ASEO,
                DEPOSITO_SEGURIDAD,
                normasExternas,
                String.join(", ", getServicios()),
                getCalificacionPromedio()
        );
    }

    /**
     * Verifica si la casa cumple con requerimientos específicos
     */
    public boolean cumpleRequisitos(boolean requierePiscina, boolean requiereEventos, int huespedes) {
        if (requierePiscina && !tienePiscina) return false;
        if (requiereEventos && !permiteEventos) return false;
        return huespedes <= getCapacidadMax().get();
    }

    @Override
    public void validar() {
        super.validar();

        if (numeroHabitaciones <= 0) {
            throw new IllegalArgumentException("Número de habitaciones debe ser positivo");
        }
        if (numeroBanos <= 0) {
            throw new IllegalArgumentException("Número de baños debe ser positivo");
        }
        if (areaTerreno <= 0 || areaConstruida <= 0) {
            throw new IllegalArgumentException("Áreas deben ser positivas");
        }
        if (areaConstruida > areaTerreno) {
            throw new IllegalArgumentException("Área construida no puede ser mayor que área de terreno");
        }
        if (capacidadParqueadero < 0) {
            throw new IllegalArgumentException("Capacidad de parqueadero no puede ser negativa");
        }
    }

    private void validarNumeroNoches(int numNoches) {
        if (numNoches <= 0) {
            throw new IllegalArgumentException("Número de noches debe ser positivo");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s - %s (Casa) %d hab. %.1f m² %s %s $%,.2f/noche",
                getNombre(),
                getCiudad(),
                numeroHabitaciones,
                areaConstruida,
                tienePiscina ? "Piscina" : "",
                tieneJardin ? "Jardín" : "",
                getPrecioNoche()
        );
    }
}