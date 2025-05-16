package co.edu.uniquindio.bookyourstay.repositorio;

import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;

public class UsuarioRepositorio extends RepositorioBase<Usuario> {
    private static UsuarioRepositorio instancia;
    private static final String ARCHIVO = "usuarios.json";

    private UsuarioRepositorio() {
        super(ARCHIVO, new TypeToken<List<Usuario>>(){}.getType());
    }

    public static UsuarioRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepositorio();
        }
        return instancia;
    }
    @Override
    public long contar() {
        return listarTodos().size();
    }
    @Override
    public Optional<Usuario> buscarPrimeroQue(CriterioBusqueda<Usuario> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .findFirst();
    }
    public Optional<Usuario> buscarPorEmail(String email) {
        return listaObjetos.stream()
                .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    protected String obtenerId(Usuario usuario) {
        return usuario.getId();
    }
}