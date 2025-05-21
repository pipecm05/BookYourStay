package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.factory.AlojamientoFactory;
import co.edu.uniquindio.bookyourstay.factory.AlojamientoFactoryProvider;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoOferta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
public class Administrador extends Usuario {

    // Métodos específicos para gestión de alojamientos

    /**
     * Crea un nuevo alojamiento en el sistema
     */
    public Alojamiento crearAlojamiento(String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios,
                                        TipoAlojamiento tipo) {
        // Validación básica (podría lanzar excepciones)
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del alojamiento es requerido");
        }

        // Factory pattern para crear el tipo correcto de alojamiento
        AlojamientoFactory factory = AlojamientoFactoryProvider.getFactory(tipo);
        return factory.crearAlojamiento(
                generarIdAlojamiento(),
                nombre,
                ciudad,
                descripcion,
                precioNoche,
                capacidadMax,
                servicios
        );
    }

    /**
     * Genera estadísticas de ocupación para un alojamiento
     */
    public EstadisticasAlojamiento generarEstadisticas(Alojamiento alojamiento,
                                                       LocalDate fechaInicio,
                                                       LocalDate fechaFin) {
        int totalDias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        int diasOcupados = alojamiento.getReservas().stream()
                .filter(r -> r.getEstado() == EstadoReserva.COMPLETADA)
                .mapToInt(r -> calcularDiasOcupacion(r, fechaInicio, fechaFin))
                .sum();

        double porcentajeOcupacion = (double) diasOcupados / totalDias * 100;
        double ganancias = calcularGanancias(alojamiento, fechaInicio, fechaFin);

        return new EstadisticasAlojamiento(
                alojamiento,
                porcentajeOcupacion,
                ganancias,
                fechaInicio,
                fechaFin
        );
    }

    // Métodos para gestión de ofertas

    /**
     * Crea una nueva oferta promocional
     */
    public Oferta crearOferta(String nombre, String descripcion, TipoOferta tipo,
                              float valor, LocalDate fechaInicio, LocalDate fechaFin,
                              List<Alojamiento> alojamientos) {
        Oferta oferta = new Oferta();
        oferta.setId(generarIdOferta());
        oferta.setNombre(nombre);
        oferta.setDescripcion(descripcion);
        oferta.setTipoOferta(tipo);
        oferta.setValor(valor);
        oferta.setFechaInicio(fechaInicio);
        oferta.setFechaFin(fechaFin);
        oferta.setAlojamientosAplicables(alojamientos);
        oferta.setActiva(true);

        return oferta;
    }

    /**
     * Desactiva una oferta existente
     */
    public void desactivarOferta(Oferta oferta) {
        oferta.setActiva(false);
    }

    // Métodos de ayuda privados

    private String generarIdAlojamiento() {
        return "ALO-" + System.currentTimeMillis();
    }

    private String generarIdOferta() {
        return "OF-" + System.currentTimeMillis();
    }

    private int calcularDiasOcupacion(Reserva reserva, LocalDate inicioPeriodo, LocalDate finPeriodo) {
        LocalDate inicio = reserva.getFechaInicio().isBefore(inicioPeriodo) ?
                inicioPeriodo : reserva.getFechaInicio();
        LocalDate fin = reserva.getFechaFin().isAfter(finPeriodo) ?
                finPeriodo : reserva.getFechaFin();

        return (int) ChronoUnit.DAYS.between(inicio, fin);
    }

    private double calcularGanancias(Alojamiento alojamiento, LocalDate inicio, LocalDate fin) {
        return alojamiento.getReservas().stream()
                .filter(r -> r.getEstado() == EstadoReserva.COMPLETADA)
                .filter(r -> !r.getFechaFin().isBefore(inicio) && !r.getFechaInicio().isAfter(fin))
                .mapToDouble(Reserva::getTotal)
                .sum();
    }

    // Clase interna para estadísticas
    @Getter
    @AllArgsConstructor
    public static class EstadisticasAlojamiento {
        private final Alojamiento alojamiento;
        private final double porcentajeOcupacion;
        private final double gananciasTotales;
        private final LocalDate fechaInicio;
        private final LocalDate fechaFin;
    }
}