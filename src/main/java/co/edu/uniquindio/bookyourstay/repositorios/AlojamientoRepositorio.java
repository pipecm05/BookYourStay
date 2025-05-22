package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AlojamientoRepositorio {
    private final ObservableList<Alojamiento> alojamientos = FXCollections.observableArrayList();

    public void guardarAlojamiento(Alojamiento alojamiento) {
        if (alojamiento == null) {
            throw new IllegalArgumentException("El alojamiento no puede ser nulo");
        }
        if (existeAlojamiento(alojamiento.getId())) {
            throw new IllegalArgumentException("Ya existe un alojamiento con el ID: " + alojamiento.getId());
        }
        alojamientos.add(alojamiento);
    }

    public Optional<Alojamiento> buscarPorId(String id) {
        return alojamientos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public ObservableList<Alojamiento> buscarPorCiudad(String ciudad) {
        return alojamientos.stream()
                .filter(a -> a.getCiudad().equalsIgnoreCase(ciudad))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public ObservableList<Alojamiento> buscarPorTipo(TipoAlojamiento tipo) {
        return alojamientos.stream()
                .filter(a -> a.getClass().getSimpleName().equalsIgnoreCase(tipo.name()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public ObservableList<Alojamiento> buscarPorCriterio(Predicate<Alojamiento> filtro) {
        return alojamientos.stream()
                .filter(filtro)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public ObservableList<Alojamiento> listarTodos() {
        return FXCollections.observableArrayList(alojamientos);
    }

    public boolean eliminarAlojamiento(String id) {
        return alojamientos.removeIf(a -> a.getId().equals(id));
    }

    public void actualizarAlojamiento(Alojamiento alojamientoActualizado) {
        Alojamiento existente = buscarPorId(alojamientoActualizado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no encontrado"));

        int index = alojamientos.indexOf(existente);
        alojamientos.set(index, alojamientoActualizado);
    }

    public boolean existeAlojamiento(String id) {
        return alojamientos.stream()
                .anyMatch(a -> a.getId().equals(id));
    }
    public Optional<Alojamiento> buscarPorNombre(String nombre) {
        return alojamientos.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
    public ObservableList<Alojamiento> buscarPorRangoPrecio(float precioMin, float precioMax) {
        return alojamientos.stream()
                .filter(a -> a.getPrecioNoche() >= precioMin && a.getPrecioNoche() <= precioMax)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public long contarAlojamientos() {
        return alojamientos.size();
    }
}