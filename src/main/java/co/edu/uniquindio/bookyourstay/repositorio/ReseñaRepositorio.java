package co.edu.uniquindio.bookyourstay.repositorio;

import co.edu.uniquindio.bookyourstay.modelo.Reseña;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReseñaRepositorio extends RepositorioBase<Reseña> {
    private static ReseñaRepositorio instancia;
    private static final String ARCHIVO = "reseñas.json";

    private ReseñaRepositorio() {
        super(ARCHIVO, new TypeToken<List<Reseña>>(){}.getType());
    }

    public static ReseñaRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new ReseñaRepositorio();
        }
        return instancia;
    }
    @Override
    public long contar() {
        return listarTodos().size();
    }
    @Override
    public Optional<Reseña> buscarPrimeroQue(CriterioBusqueda<Reseña> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .findFirst();
    }

    public List<Reseña> buscarPorAlojamiento(String idAlojamiento) {
        return listaObjetos.stream()
                .filter(resena -> resena.getAlojamiento().getId().equals(idAlojamiento))
                .collect(Collectors.toList());
    }

    public List<Reseña> buscarPorCliente(String idCliente) {
        return listaObjetos.stream()
                .filter(resena -> resena.getCliente().getId().equals(idCliente))
                .collect(Collectors.toList());
    }

    @Override
    protected String obtenerId(Reseña reseña) {
        Objects.requireNonNull(reseña, "La reseña no puede ser nula");
        return reseña.getCliente().getId() + "_" + reseña.getAlojamiento().getId();
    }
}