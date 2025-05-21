package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.repositorios.AlojamientoRepositorio;
import co.edu.uniquindio.bookyourstay.repositorios.ReservaRepositorio;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class EstadisticaServicio {
    private final ReservaRepositorio reservaRepositorio;
    private final AlojamientoRepositorio alojamientoRepositorio;

    public EstadisticaServicio(ReservaRepositorio reservaRepositorio, AlojamientoRepositorio alojamientoRepositorio) {
        this.reservaRepositorio = reservaRepositorio;
        this.alojamientoRepositorio = alojamientoRepositorio;
    }

    public Map<String, Double> obtenerOcupacionPorCiudad() {
        try {
            return alojamientoRepositorio.listarTodos().stream()
                    .collect(Collectors.groupingBy(
                            Alojamiento::getCiudad,
                            Collectors.averagingDouble(this::calcularOcupacionAlojamiento)
                    ));
        } catch (Exception e) {
            System.err.println("Error calculando ocupación por ciudad: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<TipoAlojamiento, Double> obtenerGananciasPorTipoAlojamiento() {
        try {
            return reservaRepositorio.listarTodas().stream()
                    .filter(r -> r.getAlojamiento() != null) // <-- Error común aquí
                    .collect(Collectors.groupingBy(
                            r -> obtenerTipoAlojamiento(r.getAlojamiento()),
                            Collectors.summingDouble(Reserva::getTotal)
                    ));
        } catch (Exception e) {
            System.err.println("Error calculando ganancias por tipo: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<Month, Double> obtenerGananciasMensuales(Year year) {
        try {
            return reservaRepositorio.listarTodas().stream()
                    .filter(r -> Year.from(r.getFechaInicio()).equals(year)) // <-- Cierra paréntesis
                    .collect(Collectors.groupingBy(
                            r -> r.getFechaInicio().getMonth(),
                            Collectors.summingDouble(Reserva::getTotal)
                    ));
        } catch (Exception e) {
            System.err.println("Error calculando ganancias mensuales: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public List<Alojamiento> obtenerAlojamientosMasPopulares(int limite) {
        try {
            return alojamientoRepositorio.listarTodos().stream()
                    .sorted(Comparator.comparingInt(
                            (Alojamiento a) -> reservaRepositorio.buscarPorAlojamiento(a.getId()).size()
                    ).reversed())
                    .limit(limite)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error obteniendo alojamientos populares: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Map<Month, Double> calcularTasaCancelacion(Year year) {
        try {
            Map<Month, Long> totalReservas = reservaRepositorio.listarTodas().stream()
                    .filter(r -> Year.from(r.getFechaInicio()).equals(year))
                    .collect(Collectors.groupingBy(
                            r -> r.getFechaInicio().getMonth(),
                            Collectors.counting()
                    ));

            Map<Month, Long> cancelaciones = reservaRepositorio.listarTodas().stream()
                    .filter(r -> Year.from(r.getFechaInicio()).equals(year))
                    .filter(r -> r.getEstado() == EstadoReserva.CANCELADA)
                    .collect(Collectors.groupingBy(
                            r -> r.getFechaInicio().getMonth(),
                            Collectors.counting()
                    ));

            return totalReservas.keySet().stream()
                    .collect(Collectors.toMap(
                            month -> month,
                            month -> {
                                long total = totalReservas.getOrDefault(month, 0L);
                                long canc = cancelaciones.getOrDefault(month, 0L);
                                return total > 0 ? (canc * 100.0 / total) : 0.0;
                            }
                    ));
        } catch (Exception e) {
            System.err.println("Error calculando tasa de cancelación: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<TipoAlojamiento, Double> obtenerPromedioEstadia() {
        try {
            return reservaRepositorio.listarTodas().stream()
                    .filter(r -> r.getAlojamiento() != null)
                    .collect(Collectors.groupingBy(
                            r -> obtenerTipoAlojamiento(r.getAlojamiento()),
                            Collectors.averagingInt(r -> (int) ChronoUnit.DAYS.between(
                                    r.getFechaInicio(), r.getFechaFin()))
                    ));
        } catch (Exception e) {
            System.err.println("Error calculando promedio de estadía: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<String, Object> generarReporteConsolidado(Year year) {
        Map<String, Object> reporte = new LinkedHashMap<>();
        reporte.put("gananciasMensuales", obtenerGananciasMensuales(year));
        reporte.put("ocupacionPorCiudad", obtenerOcupacionPorCiudad());
        reporte.put("gananciasPorTipo", obtenerGananciasPorTipoAlojamiento());
        reporte.put("tasaCancelacion", calcularTasaCancelacion(year));
        reporte.put("alojamientosPopulares", obtenerAlojamientosMasPopulares(5));
        reporte.put("promedioEstadia", obtenerPromedioEstadia());
        return reporte;
    }

    private double calcularOcupacionAlojamiento(Alojamiento alojamiento) {
        try {
            long diasOcupados = reservaRepositorio.buscarPorAlojamiento(alojamiento.getId()).stream()
                    .filter(r -> r.getEstado() == EstadoReserva.COMPLETADA)
                    .mapToLong(r -> ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin()))
                    .sum();

            long diasTotales = LocalDate.now().lengthOfYear();
            return diasTotales > 0 ? (diasOcupados * 100.0 / diasTotales) : 0;
        } catch (Exception e) {
            System.err.println("Error calculando ocupación: " + e.getMessage());
            return 0.0;
        }
    }

    private TipoAlojamiento obtenerTipoAlojamiento(Alojamiento alojamiento) {
        String className = alojamiento.getClass().getSimpleName().toUpperCase();
        try {
            return TipoAlojamiento.valueOf(className);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de alojamiento no soportado: " + className);
        }
    }
}