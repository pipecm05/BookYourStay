package co.edu.uniquindio.bookyourstay.repositorio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RepositorioBase<T> implements IRepositorio<T> {
    protected final String archivo;
    protected List<T> listaObjetos;
    protected final Gson gson;
    protected final Type tipoLista;

    public RepositorioBase(String archivo, Type tipoLista) {
        this.archivo = archivo;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.tipoLista = tipoLista;
        this.listaObjetos = cargarDatos();
    }

    protected List<T> cargarDatos() {
        try (Reader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, tipoLista);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    @Override
    public boolean existe(String id) {
        return listaObjetos.stream()
                .anyMatch(obj -> obtenerId(obj).equals(id));
    }

    protected void guardarDatos() {
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(listaObjetos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T guardar(T objeto) {
        listaObjetos.add(objeto);
        guardarDatos();
        return objeto;
    }

    @Override
    public Optional<T> buscarPorId(String id) {
        return listaObjetos.stream()
                .filter(obj -> obtenerId(obj).equals(id))
                .findFirst();
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(listaObjetos);
    }

    @Override
    public boolean eliminar(String id) {
        boolean eliminado = listaObjetos.removeIf(obj -> obtenerId(obj).equals(id));
        if (eliminado) {
            guardarDatos();
        }
        return eliminado;
    }

    @Override
    public T actualizar(T objeto) {
        String id = obtenerId(objeto);
        eliminar(id);
        return guardar(objeto);
    }

    @Override
    public List<T> buscarPorCriterio(CriterioBusqueda<T> criterio) {
        return listaObjetos.stream()
                .filter(criterio::cumpleCriterio)
                .collect(Collectors.toList());
    }

    protected abstract String obtenerId(T objeto);
}