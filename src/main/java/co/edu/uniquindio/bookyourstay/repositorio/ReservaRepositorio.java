package co.edu.uniquindio.bookyourstay.repositorio;

import co.edu.uniquindio.bookyourstay.modelo.Oferta;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservaRepositorio extends RepositorioBase<Reserva> {
    private static ReservaRepositorio instancia;
    private static final String ARCHIVO = "reservas.json";

    private ReservaRepositorio() {
        super(ARCHIVO, new TypeToken<List<Reserva>>(){}.getType());
    }

    public static ReservaRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new ReservaRepositorio();
        }
        return instancia;
    }

    @Override
    public long contar() {
        return listarTodos().size();
    }

    @Override
    public Optional<Reserva> buscarPrimeroQue(CriterioBusqueda<Reserva> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .findFirst();
    }

    public List<Reserva> buscarPorCliente(String idCliente) {
        return listaObjetos.stream()
                .filter(reserva -> reserva.getCliente().getId().equals(idCliente))
                .collect(Collectors.toList());
    }

    public List<Reserva> buscarPorAlojamiento(String idAlojamiento) {
        return listaObjetos.stream()
                .filter(reserva -> reserva.getAlojamiento().getId().equals(idAlojamiento))
                .collect(Collectors.toList());
    }

    public boolean existeReservaEnFecha(String idAlojamiento, LocalDate fechaInicio, LocalDate fechaFin) {
        return listaObjetos.stream()
                .anyMatch(reserva ->
                        reserva.getAlojamiento().getId().equals(idAlojamiento) &&
                                !reserva.getFechaFin().isBefore(fechaInicio) &&
                                !reserva.getFechaInicio().isAfter(fechaFin)
                );
    }

    @Override
    protected String obtenerId(Reserva reserva) {
        return reserva.getId();
    }
}