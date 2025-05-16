package co.edu.uniquindio.bookyourstay.repositorio;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlojamientoRepositorio extends RepositorioBase<Alojamiento> {
    private static AlojamientoRepositorio instancia;
    private static final String ARCHIVO = "alojamientos.json";

    private AlojamientoRepositorio() {
        super(ARCHIVO, new TypeToken<List<Alojamiento>>(){}.getType());
    }

    public static AlojamientoRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new AlojamientoRepositorio();
        }
        return instancia;
    }
    @Override
    public long contar() {
        return listarTodos().size();
    }
    @Override
    public Optional<Alojamiento> buscarPrimeroQue(CriterioBusqueda<Alojamiento> criterio) {
        return listarTodos().stream()
                .filter(criterio::cumpleCriterio)
                .findFirst();
    }

    @Override
    protected String obtenerId(Alojamiento alojamiento) {
        return alojamiento.getId();
    }

    // Métodos específicos para alojamientos
    public List<Alojamiento> buscarPorCiudad(String ciudad) {
        return listarTodos().stream()
                .filter(a -> a.getCiudad().equalsIgnoreCase(ciudad))
                .collect(Collectors.toList());
    }

    public List<Alojamiento> buscarPorNombre(String nombre) {
        return listarTodos().stream()
                .filter(a -> a.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<String> obtenerCiudades() {
        return listarTodos().stream()
                .map(Alojamiento::getCiudad)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Alojamiento> buscarPorTipo(String tipo) {
        return listarTodos().stream()
                .filter(a -> a.getClass().getSimpleName().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    public List<Alojamiento> buscarPorRangoPrecio(double min, double max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Los precios no pueden ser negativos");
        }

        final double precioMin = Math.min(min, max); // Asegura que min sea el menor
        final double precioMax = Math.max(min, max); // Asegura que max sea el mayor

        return listarTodos().stream()
                .filter(a -> a.getPrecioNoche() >= precioMin && a.getPrecioNoche() <= precioMax)
                .collect(Collectors.toList());
    }

    public List<Alojamiento> buscarPorServicio(String servicio) {
        return listarTodos().stream()
                .filter(a -> a.getServicios().stream()
                        .anyMatch(s -> s.equalsIgnoreCase(servicio)))
                .collect(Collectors.toList());
    }

    public List<Alojamiento> buscarPorCapacidad(int capacidadMinima) {
        return listarTodos().stream()
                .filter(a -> a.getCapacidad() >= capacidadMinima)
                .collect(Collectors.toList());
    }

    // Métodos para estadísticas
    public long contarPorCiudad(String ciudad) {
        return listarTodos().stream()
                .filter(a -> a.getCiudad().equalsIgnoreCase(ciudad))
                .count();
    }

    public double promedioPrecioPorCiudad(String ciudad) {
        return listarTodos().stream()
                .filter(a -> a.getCiudad().equalsIgnoreCase(ciudad))
                .mapToDouble(Alojamiento::getPrecioNoche)
                .average()
                .orElse(0.0);
    }
}