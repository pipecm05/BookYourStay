package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Billetera;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Transaccion;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BilleteraRepositorio extends RepositorioBase<Billetera> {
    private static BilleteraRepositorio instancia;
    private static final String ARCHIVO = "billeteras.json";

    private BilleteraRepositorio() {
        super(ARCHIVO, new TypeToken<List<Billetera>>(){}.getType());
    }

    public static synchronized BilleteraRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new BilleteraRepositorio();
        }
        return instancia;
    }

    @Override
    public long contar() {
        return listarTodos().size();
    }
    public interface CriterioBusqueda<T> {
        boolean cumpleCriterio(T elemento);
    }
    @Override
    public Optional<Billetera> buscarPrimeroQue(CriterioBusqueda<Billetera> criterio) {
        if (criterio == null) {
            throw new IllegalArgumentException("El criterio no puede ser nulo");
        }

        return listarTodos().stream()
                .filter(billetera -> criterio.cumpleCriterio(billetera))
                .findFirst();
    }

    public Optional<Billetera> buscarPorCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente o ID del cliente no puede ser nulo");
        }
        return buscarPorClienteId(cliente.getId());
    }

    public Optional<Billetera> buscarPorClienteId(String clienteId) {
        return listaObjetos.stream()
                .filter(b -> clienteId.equals(obtenerId(b)))
                .findFirst();
    }

    public List<Billetera> buscarPorSaldoMinimo(float saldoMinimo) {
        return listarTodos().stream()
                .filter(b -> b.getSaldo() >= saldoMinimo)
                .collect(Collectors.toList());
    }

    public List<Transaccion> obtenerTransaccionesPorCliente(String clienteId) {
        return buscarPorClienteId(clienteId)
                .map(Billetera::getHistorialTransacciones)
                .orElse(List.of());
    }

    public List<Transaccion> filtrarTransacciones(String clienteId, Predicate<Transaccion> filtro) {
        return obtenerTransaccionesPorCliente(clienteId).stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    public Optional<Float> obtenerSaldoCliente(String clienteId) {
        return buscarPorClienteId(clienteId)
                .map(Billetera::getSaldo);
    }

    @Override
    protected String obtenerId(Billetera billetera) {
        if (billetera == null) {
            throw new IllegalArgumentException("La billetera no puede ser nula");
        }
        return billetera.getNumeroCuenta();
    }

    @Override
    public void guardar(Billetera billetera) {
        if (billetera == null) {
            throw new IllegalArgumentException("La billetera no puede ser nula");
        }
        super.guardar(billetera);
    }

    public boolean existeBilletera(String clienteId) {
        return buscarPorClienteId(clienteId).isPresent();
    }

    public void actualizarBilletera(Billetera billetera) {
        if (billetera == null) {
            throw new IllegalArgumentException("La billetera no puede ser nula");
        }
        eliminar(billetera.getNumeroCuenta());
        guardar(billetera);
    }
}