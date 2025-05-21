package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Billetera;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class RepositorioBase<T> {
    protected List<T> listaObjetos;
    private final String archivo;
    private final Type tipoLista;
    private final Gson gson;

    protected RepositorioBase(String archivo, Type tipoLista) {
        this.archivo = archivo;
        this.tipoLista = tipoLista;
        this.gson = new Gson();
        this.listaObjetos = cargarDatos();
    }

    public abstract Optional<Billetera> buscarPrimeroQue(BilleteraRepositorio.CriterioBusqueda<Billetera> criterio);

    protected abstract String obtenerId(T objeto);

    public void guardar(T objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo");
        }

        eliminar(obtenerId(objeto));
        listaObjetos.add(objeto);
        guardarDatos();
    }

    public Optional<T> buscarPorId(String id) {
        return listaObjetos.stream()
                .filter(obj -> obtenerId(obj).equals(id))
                .findFirst();
    }

    public boolean eliminar(String id) {
        boolean eliminado = listaObjetos.removeIf(obj -> obtenerId(obj).equals(id));
        if (eliminado) {
            guardarDatos();
        }
        return eliminado;
    }

    public List<T> listarTodos() {
        return new ArrayList<>(listaObjetos);
    }

    public long contar() {
        return listaObjetos.size();
    }

    public Optional<T> buscarPrimeroQue(Predicate<T> criterio) {
        return listaObjetos.stream()
                .filter(criterio)
                .findFirst();
    }

    private List<T> cargarDatos() {
        try (Reader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, tipoLista);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar datos del archivo: " + archivo, e);
        }
    }

    private void guardarDatos() {
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(listaObjetos, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar datos en el archivo: " + archivo, e);
        }
    }

    public void actualizar(T objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo");
        }

        String id = obtenerId(objeto);
        eliminar(id);
        guardar(objeto);
    }
}
