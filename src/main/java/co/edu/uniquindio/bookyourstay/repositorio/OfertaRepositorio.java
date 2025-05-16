package co.edu.uniquindio.bookyourstay.repositorio;

import co.edu.uniquindio.bookyourstay.modelo.Oferta;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OfertaRepositorio extends RepositorioBase<Oferta> implements IRepositorio<Oferta> {
    private static OfertaRepositorio instancia;
    private static final String ARCHIVO = "ofertas.json";

    private OfertaRepositorio() {
        super(ARCHIVO, new TypeToken<List<Oferta>>(){}.getType());
    }

    public static OfertaRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new OfertaRepositorio();
        }
        return instancia;
    }
    @Override
    public long contar() {
        return listarTodos().size();
    }
    @Override
    public Optional<Oferta> buscarPrimeroQue(CriterioBusqueda<Oferta> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .findFirst();
    }

    @Override
    protected String obtenerId(Oferta oferta) {
        return oferta.getAlojamiento().getId() + "_" + oferta.getFechaInicio();
    }

    @Override
    public List<Oferta> buscarPorCriterio(CriterioBusqueda<Oferta> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .collect(Collectors.toList());
    }

    // Métodos específicos para ofertas
    public List<Oferta> buscarOfertasVigentes(LocalDate fecha) {
        return buscarPorCriterio(oferta ->
                !fecha.isBefore(oferta.getFechaInicio()) &&
                        !fecha.isAfter(oferta.getFechaFin())
        );
    }

    public List<Oferta> buscarOfertasPorAlojamiento(String idAlojamiento) {
        return buscarPorCriterio(oferta ->
                oferta.getAlojamiento().getId().equals(idAlojamiento)
        );
    }

    public List<Oferta> buscarOfertasPorDescuento(double descuentoMinimo) {
        return buscarPorCriterio(oferta ->
                oferta.getDescuento() >= descuentoMinimo
        );
    }
}