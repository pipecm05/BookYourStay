package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.factory.AlojamientoFactoryProvider;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.repositorios.AlojamientoRepositorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlojamientoServicio {
    private static AlojamientoServicio instancia;
    private final AlojamientoRepositorio alojamientoRepositorio;

    private AlojamientoServicio() {
        this.alojamientoRepositorio = new AlojamientoRepositorio();
    }

    public static synchronized AlojamientoServicio obtenerInstancia() {
        if (instancia == null) {
            instancia = new AlojamientoServicio();
        }
        return instancia;
    }

    public ObservableList<Alojamiento> listarTodos() {
        return FXCollections.observableArrayList(alojamientoRepositorio.listarTodos());
    }

    public Alojamiento crearAlojamiento(TipoAlojamiento tipo, String nombre, String ciudad,
                                        String descripcion, float precioNoche, int capacidadMax,
                                        List<String> servicios) {
        validarDatosAlojamiento(nombre, ciudad, precioNoche, capacidadMax);

        if (existeAlojamiento(nombre, ciudad)) {
            throw new IllegalArgumentException("Ya existe un alojamiento con ese nombre en la ciudad especificada");
        }

        Alojamiento alojamiento = AlojamientoFactoryProvider.getFactory(tipo)
                .crearAlojamiento(generarId(), nombre, ciudad, descripcion, precioNoche, capacidadMax, servicios);

        alojamientoRepositorio.guardarAlojamiento(alojamiento);
        return alojamiento;
    }

    public Alojamiento actualizarAlojamiento(String id, Alojamiento nuevosDatos) {
        validarDatosAlojamiento(nuevosDatos.getNombre(), nuevosDatos.getCiudad(),
                nuevosDatos.getPrecioNoche(), nuevosDatos.getCapacidadMax());

        Alojamiento alojamiento = obtenerAlojamiento(id);
        alojamiento.setNombre(nuevosDatos.getNombre());
        alojamiento.setCiudad(nuevosDatos.getCiudad());
        alojamiento.setDescripcion(nuevosDatos.getDescripcion());
        alojamiento.setPrecioNoche(nuevosDatos.getPrecioNoche());
        alojamiento.setCapacidadMax(nuevosDatos.getCapacidadMax());
        alojamiento.setServicios(nuevosDatos.getServicios());

        alojamientoRepositorio.actualizarAlojamiento(alojamiento);
        return alojamiento;
    }

    public Alojamiento obtenerAlojamiento(String id) {
        return alojamientoRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el alojamiento con ID: " + id));
    }

    public boolean eliminarAlojamiento(String id) {
        try {
            return alojamientoRepositorio.eliminarAlojamiento(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar alojamiento: " + e.getMessage(), e);
        }
    }

    public ObservableList<Alojamiento> buscarPorCiudad(String ciudad) {
        return FXCollections.observableArrayList(alojamientoRepositorio.buscarPorCiudad(ciudad));
    }

    public ObservableList<Alojamiento> buscarPorTipo(TipoAlojamiento tipo) {
        return FXCollections.observableArrayList(alojamientoRepositorio.buscarPorTipo(tipo));
    }

    public ObservableList<Alojamiento> buscarPorRangoPrecio(float precioMin, float precioMax) {
        return FXCollections.observableArrayList(alojamientoRepositorio.buscarPorRangoPrecio(precioMin, precioMax));
    }

    public ObservableList<Alojamiento> obtenerMejoresAlojamientos(int limite) {
        return FXCollections.observableArrayList(alojamientoRepositorio.listarTodos().stream()
                .sorted((a1, a2) -> Float.compare(a2.getCalificacionPromedio().get(), a1.getCalificacionPromedio().get()))
                .limit(limite)
                .collect(Collectors.toList()));
    }

    private String generarId() {
        return "ALO-" + System.currentTimeMillis();
    }

    private boolean existeAlojamiento(String nombre, String ciudad) {
        return alojamientoRepositorio.listarTodos().stream()
                .anyMatch(a -> a.getNombre().equalsIgnoreCase(nombre) &&
                        a.getCiudad().equalsIgnoreCase(ciudad));
    }

    private void validarDatosAlojamiento(String nombre, String ciudad,
                                         float precioNoche, int capacidadMax) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del alojamiento es requerido");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad es requerida");
        }
        if (precioNoche <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor a cero");
        }
        if (capacidadMax <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a cero");
        }
    }
}