package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Oferta;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoOferta;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoOferta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OfertaRepositorio {
    private final List<Oferta> ofertas = new ArrayList<>();

    /**
     * Guarda una nueva oferta en el repositorio
     * @param oferta La oferta a guardar
     * @throws IllegalArgumentException si la oferta es nula o ya existe
     */
    public void guardarOferta(Oferta oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula");
        }
        if (existeOferta(oferta.getId())) {
            throw new IllegalArgumentException("Ya existe una oferta con el ID: " + oferta.getId());
        }
        ofertas.add(oferta);
    }

    /**
     * Busca una oferta por su ID
     * @param id El ID de la oferta a buscar
     * @return Optional con la oferta si existe
     */
    public Optional<Oferta> buscarPorId(String id) {
        return ofertas.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    /**
     * Obtiene todas las ofertas registradas
     * @return Lista inmutable de todas las ofertas
     */
    public List<Oferta> listarTodas() {
        return new ArrayList<>(ofertas);
    }

    /**
     * Busca ofertas por tipo
     * @param tipo El tipo de oferta (PORCENTAJE, MONTO_FIJO)
     * @return Lista de ofertas del tipo especificado
     */
    public List<Oferta> buscarPorTipo(TipoOferta tipo) {
        return ofertas.stream()
                .filter(o -> o.getTipoOferta() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las ofertas actualmente vigentes
     * @return Lista de ofertas activas dentro de su periodo de vigencia
     */
    public List<Oferta> listarOfertasVigentes() {
        LocalDate hoy = LocalDate.now();
        return ofertas.stream()
                .filter(o -> o.getFechaInicio().isBefore(hoy) || o.getFechaInicio().isEqual(hoy))
                .filter(o -> o.getFechaFin().isAfter(hoy) || o.getFechaFin().isEqual(hoy))
                .filter(Oferta::isActiva)
                .collect(Collectors.toList());
    }

    /**
     * Busca ofertas por estado
     * @param estado El estado de la oferta (ACTIVA, INACTIVA)
     * @return Lista de ofertas con el estado especificado
     */
    public List<Oferta> buscarPorEstado(boolean estado) {
        return ofertas.stream()
                .filter(o -> o.getEstado() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una oferta del repositorio
     * @param id El ID de la oferta a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean eliminarOferta(String id) {
        return ofertas.removeIf(o -> o.getId().equals(id));
    }

    /**
     * Actualiza una oferta existente
     * @param ofertaActualizada La oferta con los datos actualizados
     * @throws IllegalArgumentException si la oferta no existe
     */
    public void actualizarOferta(Oferta ofertaActualizada) {
        Oferta existente = buscarPorId(ofertaActualizada.getId())
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

        int index = ofertas.indexOf(existente);
        ofertas.set(index, ofertaActualizada);
    }

    /**
     * Verifica si existe una oferta con el ID especificado
     * @param id El ID a verificar
     * @return true si existe, false si no
     */
    public boolean existeOferta(String id) {
        return ofertas.stream()
                .anyMatch(o -> o.getId().equals(id));
    }

    /**
     * Obtiene ofertas que aplican a un alojamiento específico
     * @param alojamientoId ID del alojamiento
     * @return Lista de ofertas aplicables al alojamiento
     */
    public List<Oferta> buscarOfertasPorAlojamiento(String alojamientoId) {
        return ofertas.stream()
                .filter(o -> o.getAlojamientosAplicables().stream()
                        .anyMatch(a -> a.getId().equals(alojamientoId)))
                .collect(Collectors.toList());
    }

    /**
     * Busca ofertas que cumplan con un criterio personalizado
     * @param criterio Predicado para filtrar las ofertas
     * @return Lista de ofertas que cumplen el criterio
     */
    public List<Oferta> buscarPorCriterio(Predicate<Oferta> criterio) {
        return ofertas.stream()
                .filter(criterio)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta el total de ofertas registradas
     * @return El número total de ofertas
     */
    public long contarOfertas() {
        return ofertas.size();
    }
}
