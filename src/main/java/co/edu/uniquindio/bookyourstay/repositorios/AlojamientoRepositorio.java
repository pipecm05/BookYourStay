package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AlojamientoRepositorio {
    private final List<Alojamiento> alojamientos = new ArrayList<>();

    /**
     * Guarda un alojamiento en el repositorio
     * @param alojamiento El alojamiento a guardar
     * @throws IllegalArgumentException si el alojamiento es nulo o ya existe
     */
    public void guardarAlojamiento(Alojamiento alojamiento) {
        if (alojamiento == null) {
            throw new IllegalArgumentException("El alojamiento no puede ser nulo");
        }
        if (existeAlojamiento(alojamiento.getId())) {
            throw new IllegalArgumentException("Ya existe un alojamiento con el ID: " + alojamiento.getId());
        }
        alojamientos.add(alojamiento);
    }

    /**
     * Busca un alojamiento por su ID
     * @param id El ID del alojamiento a buscar
     * @return Optional con el alojamiento si existe
     */
    public Optional<Alojamiento> buscarPorId(String id) {
        return alojamientos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    /**
     * Busca alojamientos por ciudad
     * @param ciudad La ciudad para filtrar
     * @return Lista de alojamientos en la ciudad especificada
     */
    public List<Alojamiento> buscarPorCiudad(String ciudad) {
        return alojamientos.stream()
                .filter(a -> a.getCiudad().equalsIgnoreCase(ciudad))
                .collect(Collectors.toList());
    }

    /**
     * Busca alojamientos por tipo
     * @param tipo El tipo de alojamiento (CASA, APARTAMENTO, HOTEL)
     * @return Lista de alojamientos del tipo especificado
     */
    public List<Alojamiento> buscarPorTipo(TipoAlojamiento tipo) {
        return alojamientos.stream()
                .filter(a -> a.getClass().getSimpleName().equalsIgnoreCase(tipo.name()))
                .collect(Collectors.toList());
    }

    /**
     * Busca alojamientos que cumplan con un predicado específico
     * @param filtro Predicado para filtrar los alojamientos
     * @return Lista de alojamientos que cumplen el criterio
     */
    public List<Alojamiento> buscarPorCriterio(Predicate<Alojamiento> filtro) {
        return alojamientos.stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los alojamientos disponibles
     * @return Lista inmutable de todos los alojamientos
     */
    public List<Alojamiento> listarTodos() {
        return new ArrayList<>(alojamientos);
    }

    /**
     * Elimina un alojamiento del repositorio
     * @param id El ID del alojamiento a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean eliminarAlojamiento(String id) {
        return alojamientos.removeIf(a -> a.getId().equals(id));
    }

    /**
     * Actualiza un alojamiento existente
     * @param alojamientoActualizado El alojamiento con los datos actualizados
     * @throws IllegalArgumentException si el alojamiento no existe
     */
    public void actualizarAlojamiento(Alojamiento alojamientoActualizado) {
        Alojamiento existente = buscarPorId(alojamientoActualizado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no encontrado"));

        int index = alojamientos.indexOf(existente);
        alojamientos.set(index, alojamientoActualizado);
    }

    /**
     * Verifica si existe un alojamiento con el ID especificado
     * @param id El ID a verificar
     * @return true si existe, false si no
     */
    public boolean existeAlojamiento(String id) {
        return alojamientos.stream()
                .anyMatch(a -> a.getId().equals(id));
    }

    /**
     * Cuenta el total de alojamientos en el repositorio
     * @return El número total de alojamientos
     */
    public long contarAlojamientos() {
        return alojamientos.size();
    }

    /**
     * Busca alojamientos por rango de precios
     * @param precioMin Precio mínimo (inclusive)
     * @param precioMax Precio máximo (inclusive)
     * @return Lista de alojamientos en el rango de precios
     */
    public List<Alojamiento> buscarPorRangoPrecio(float precioMin, float precioMax) {
        return alojamientos.stream()
                .filter(a -> a.getPrecioNoche() >= precioMin && a.getPrecioNoche() <= precioMax)
                .collect(Collectors.toList());
    }
}