package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import co.edu.uniquindio.bookyourstay.repositorio.AlojamientoRepositorio;
import co.edu.uniquindio.bookyourstay.repositorio.ReservaRepositorio;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaServicio {
    private static EstadisticaServicio instancia;
    private final ReservaRepositorio reservaRepositorio;
    private final AlojamientoRepositorio alojamientoRepositorio;

    private EstadisticaServicio() {
        this.reservaRepositorio = ReservaRepositorio.getInstancia();
        this.alojamientoRepositorio = AlojamientoRepositorio.getInstancia();
    }

    public static EstadisticaServicio getInstancia() {
        if (instancia == null) {
            instancia = new EstadisticaServicio();
        }
        return instancia;
    }

    public Map<String, Double> obtenerOcupacionPorCiudad() {
        List<Alojamiento> alojamientos = alojamientoRepositorio.listarTodos();
        Map<String, Double> ocupacionPorCiudad = new HashMap<>();

        alojamientos.forEach(alojamiento -> {
            List<Reserva> reservas = reservaRepositorio.buscarPorAlojamiento(alojamiento.getId());
            double porcentajeOcupacion = calcularPorcentajeOcupacion(reservas);
            ocupacionPorCiudad.merge(
                    alojamiento.getCiudad(),
                    porcentajeOcupacion,
                    Double::sum
            );
        });

        return ocupacionPorCiudad;
    }

    public Map<String, Double> obtenerGananciasPorTipoAlojamiento() {
        return alojamientoRepositorio.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        alojamiento -> alojamiento.getClass().getSimpleName(),
                        Collectors.summingDouble(alojamiento ->
                                reservaRepositorio.buscarPorAlojamiento(alojamiento.getId())
                                        .stream()
                                        .mapToDouble(Reserva::getTotal)
                                        .sum()
                        )
                ));
    }

    public Map<Month, Double> obtenerGananciasMensuales(Year year) {
        return reservaRepositorio.listarTodos().stream()
                .filter(reserva -> Year.from(reserva.getFechaInicio()).equals(year))
                .collect(Collectors.groupingBy(
                        reserva -> reserva.getFechaInicio().getMonth(),
                        Collectors.summingDouble(Reserva::getTotal)
                ));
    }

    private double calcularPorcentajeOcupacion(List<Reserva> reservas) {
        long diasOcupados = reservas.stream()
                .mapToLong(r -> r.getFechaInicio().until(r.getFechaFin()).getDays())
                .sum();

        long diasTotales = LocalDate.now().lengthOfYear();
        return (double) diasOcupados / diasTotales * 100;
    }
}