package co.edu.uniquindio.bookyourstay.repositorio;

import co.edu.uniquindio.bookyourstay.modelo.Billetera;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;

public class BilleteraRepositorio extends RepositorioBase<Billetera> {
    private static BilleteraRepositorio instancia;
    private static final String ARCHIVO = "billeteras.json";

    private BilleteraRepositorio() {
        super(ARCHIVO, new TypeToken<List<Billetera>>(){}.getType());
    }

    public static BilleteraRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new BilleteraRepositorio();
        }
        return instancia;
    }
    @Override
    public long contar() {
        return listarTodos().size(); // Cuenta todas las billeteras registradas
    }
    @Override
    public Optional<Billetera> buscarPrimeroQue(CriterioBusqueda<Billetera> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .findFirst();
    }

    public Optional<Billetera> buscarPorCliente(Cliente cliente) {
        return listaObjetos.stream()
                .filter(b -> obtenerId(b).equals(cliente.getId()))
                .findFirst();
    }

    @Override
    protected String obtenerId(Billetera billetera) {
        // Asumimos que cada billetera tiene el mismo ID que su cliente
        return billetera.getClienteId();
    }
}