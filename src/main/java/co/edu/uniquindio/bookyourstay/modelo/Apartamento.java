package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Apartamento extends Alojamiento {
    private static final float COSTO_MANTENIMIENTO = 30000; // Costo adicional por mantenimiento
    private static final float DEPOSITO_SEGURIDAD = 100000; // Depósito reembolsable
    private int numeroHabitaciones;
    private int numeroBanos;
    private boolean tieneBalcon;
    private boolean permiteMascotas;
    private float area; // metros cuadrados
    private String normasConvivencia;

    @Override
    public float calcularCostoTotal(int numNoches) {
        if (numNoches <= 0) {
            throw new IllegalArgumentException("El número de noches debe ser positivo");
        }
        return calcularCostoBase(numNoches) + COSTO_MANTENIMIENTO + DEPOSITO_SEGURIDAD;
    }

    /**
     * Calcula el costo de limpieza adicional basado en el área del apartamento
     * @return Costo de limpieza
     */
    public float calcularCostoLimpieza() {
        return area * 500; // $500 por m²
    }
    public Apartamento(String nombre, String ciudad, String descripcion,
                       int capacidadMax, float precioNoche) {
        super(
                nombre,
                ciudad,
                UsuarioActual.getInstancia().getUsuario(), // Propietario
                descripcion,
                TipoAlojamiento.APARTAMENTO, // Tipo específico
                precioNoche,
                capacidadMax
        );
    }
    /**
     * Verifica si el apartamento cumple con requerimientos específicos
     * @param requiereBalcon Indica si se requiere balcón
     * @param aceptaMascotas Indica si se necesitan permitir mascotas
     * @return true si cumple con los requerimientos
     */
    public boolean cumpleRequisitos(boolean requiereBalcon, boolean aceptaMascotas) {
        if (requiereBalcon && !tieneBalcon) {
            return false;
        }
        if (aceptaMascotas && !permiteMascotas) {
            return false;
        }
        return true;
    }

    /**
     * Genera un reporte detallado del apartamento
     * @return String con la información detallada
     */
    public String generarReporte() {
        return String.format(
                "Apartamento: %s\n" +
                        "Ubicación: %s\n" +
                        "Habitaciones: %d\n" +
                        "Baños: %d\n" +
                        "Área: %.2f m²\n" +
                        "Balcón: %s\n" +
                        "Mascotas: %s\n" +
                        "Precio base por noche: $%,.2f\n" +
                        "Costo mantenimiento: $%,.2f\n" +
                        "Depósito seguridad: $%,.2f\n" +
                        "Normas de convivencia: %s\n" +
                        "Servicios incluidos: %s\n" +
                        "Calificación promedio: %.1f/5",
                getNombre(),
                getCiudad(),
                numeroHabitaciones,
                numeroBanos,
                area,
                tieneBalcon ? "Sí" : "No",
                permiteMascotas ? "Permitidas" : "No permitidas",
                getPrecioNoche(),
                COSTO_MANTENIMIENTO,
                DEPOSITO_SEGURIDAD,
                normasConvivencia,
                String.join(", ", getServicios()),
                getCalificacionPromedio()
        );
    }

    /**
     * Aplica normas de convivencia específicas para apartamentos
     * @param normas Lista de normas a aplicar
     */
    public void aplicarNormasConvivencia(List<String> normas) {
        if (normas == null || normas.isEmpty()) {
            this.normasConvivencia = "Normas estándar de convivencia";
        } else {
            this.normasConvivencia = String.join("\n- ", normas);
        }
    }

    @Override
    public void validar() {
        super.validar(); // Validaciones básicas de Alojamiento

        if (numeroHabitaciones <= 0) {
            throw new IllegalArgumentException("El número de habitaciones debe ser positivo");
        }
        if (numeroBanos <= 0) {
            throw new IllegalArgumentException("El número de baños debe ser positivo");
        }
        if (area <= 0) {
            throw new IllegalArgumentException("El área debe ser positiva");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s - %s (Apartamento) %d hab. %d baños %.1f m² $%,.2f/noche",
                getNombre(),
                getCiudad(),
                numeroHabitaciones,
                numeroBanos,
                area,
                getPrecioNoche()
        );
    }
}