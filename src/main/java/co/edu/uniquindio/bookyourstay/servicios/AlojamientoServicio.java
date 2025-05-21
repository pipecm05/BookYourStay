package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.factory.AlojamientoFactoryProvider;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.repositorios.AlojamientoRepositorio;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AlojamientoServicio {
    private static AlojamientoServicio instancia;
    private final AlojamientoRepositorio alojamientoRepositorio;

    private AlojamientoServicio(AlojamientoRepositorio alojamientoRepositorio) {
        this.alojamientoRepositorio = alojamientoRepositorio;
    }
    public static synchronized AlojamientoServicio obtenerInstancia() {
        if (instancia == null) {
            instancia = new AlojamientoServicio(new AlojamientoRepositorio());
        }
        return instancia;
    }

    /**
     * Crea un nuevo alojamiento en el sistema
     * @param tipo Tipo de alojamiento (CASA, APARTAMENTO, HOTEL)
     * @param nombre Nombre del alojamiento
     * @param ciudad Ciudad donde se encuentra
     * @param descripcion Descripción detallada
     * @param precioNoche Precio por noche
     * @param capacidadMax Capacidad máxima de huéspedes
     * @param servicios Lista de servicios ofrecidos
     * @return El alojamiento creado
     * @throws IllegalArgumentException Si ya existe un alojamiento con el mismo nombre en la misma ciudad
     * @throws IllegalStateException Si los datos no cumplen con las validaciones
     */
    public Alojamiento crearAlojamiento(String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios,
                                        TipoAlojamiento tipo)  throws IllegalArgumentException, IllegalStateException {

        validarDatosAlojamiento(nombre, ciudad, precioNoche, capacidadMax);

        if (existeAlojamiento(nombre, ciudad)) {
            throw new IllegalArgumentException("Ya existe un alojamiento con ese nombre en la ciudad especificada");
        }

        Alojamiento alojamiento = AlojamientoFactoryProvider.getFactory(tipo)
                .crearAlojamiento(generarId(), nombre, ciudad, descripcion, precioNoche, capacidadMax, servicios);

        alojamientoRepositorio.guardarAlojamiento(alojamiento);
        return alojamiento;
    }

    /**
     * Actualiza un alojamiento existente
     * @param id ID del alojamiento a actualizar
     * @param nuevosDatos Objeto con los nuevos datos
     * @return Alojamiento actualizado
     * @throws IllegalStateException Si los nuevos datos no son válidos
     * @throws NoSuchElementException Si no se encuentra el alojamiento
     */
    public Alojamiento actualizarAlojamiento(String id, Alojamiento nuevosDatos) throws IllegalStateException, NoSuchElementException {
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

    /**
     * Obtiene un alojamiento por su ID
     * @param id ID del alojamiento
     * @return El alojamiento encontrado
     * @throws NoSuchElementException Si no se encuentra el alojamiento
     */
    public Alojamiento obtenerAlojamiento(String id) throws NoSuchElementException {
        return alojamientoRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el alojamiento con ID: " + id));
    }

    /**
     * Elimina un alojamiento del sistema
     * @param id ID del alojamiento a eliminar
     * @return true si se eliminó correctamente
     * @throws NoSuchElementException Si no se encuentra el alojamiento
     */
    public boolean eliminarAlojamiento(String id) throws NoSuchElementException {
        if (!alojamientoRepositorio.existeAlojamiento(id)) {
            throw new NoSuchElementException("No se puede eliminar, el alojamiento no existe");
        }
        return alojamientoRepositorio.eliminarAlojamiento(id);
    }

    // Métodos que no lanzan excepciones (mantienen su implementación original)
    public List<Alojamiento> buscarPorCiudad(String ciudad) {
        return alojamientoRepositorio.buscarPorCiudad(ciudad);
    }

    public List<Alojamiento> buscarPorTipo(TipoAlojamiento tipo) {
        return alojamientoRepositorio.buscarPorTipo(tipo);
    }

    public List<Alojamiento> listarTodos() {
        return alojamientoRepositorio.listarTodos();
    }

    public List<Alojamiento> buscarPorRangoPrecio(float precioMin, float precioMax) {
        return alojamientoRepositorio.buscarPorRangoPrecio(precioMin, precioMax);
    }

    public List<Alojamiento> obtenerMejoresAlojamientos(int limite) {
        return alojamientoRepositorio.listarTodos().stream()
                .sorted((a1, a2) -> Float.compare(a2.getCalificacionPromedio(), a1.getCalificacionPromedio()))
                .limit(limite)
                .collect(Collectors.toList());
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
                                         float precioNoche, int capacidadMax) throws IllegalStateException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalStateException("El nombre del alojamiento es requerido");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalStateException("La ciudad es requerida");
        }
        if (precioNoche <= 0) {
            throw new IllegalStateException("El precio por noche debe ser mayor a cero");
        }
        if (capacidadMax <= 0) {
            throw new IllegalStateException("La capacidad máxima debe ser mayor a cero");
        }
    }
}