package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Reseña;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCalificacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReseñaRepositorio {
    private final List<Reseña> reseñas = new ArrayList<>();

    /**
     * Guarda una reseña en el repositorio
     * @param reseña La reseña a guardar
     * @throws IllegalArgumentException si la reseña es nula o ya existe
     */
    public void guardarResena(Reseña reseña) {
        if (reseña == null) {
            throw new IllegalArgumentException("La reseña no puede ser nula");
        }
        if (existeReseña(reseña.getId())) {
            throw new IllegalArgumentException("Ya existe una reseña con el ID: " + reseña.getId());
        }
        reseñas.add(reseña);
    }

    /**
     * Busca una reseña por su ID
     * @param id ID de la reseña
     * @return Optional con la reseña si existe
     */
    public Optional<Reseña> buscarPorId(String id) {
        return reseñas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    /**
     * Busca reseñas por alojamiento
     * @param alojamientoId ID del alojamiento
     * @return Lista de reseñas para el alojamiento
     */
    public List<Reseña> buscarPorAlojamiento(String alojamientoId) {
        return reseñas.stream()
                .filter(r -> r.getAlojamiento().getId().equals(alojamientoId))
                .collect(Collectors.toList());
    }
    public Optional<Reseña> buscarPorReserva(String reservaId) {
        return reseñas.stream()
                .filter(r -> r.getReserva() != null && r.getReserva().getId().equals(reservaId))
                .findFirst();
    }
    /**
     * Busca reseñas por cliente
     * @param clienteId ID del cliente
     * @return Lista de reseñas del cliente
     */
    public List<Reseña> buscarPorCliente(String clienteId) {
        return reseñas.stream()
                .filter(r -> r.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reseñas
     * @return Lista inmutable de todas las reseñas
     */
    public List<Reseña> listarTodas() {
        return new ArrayList<>(reseñas);
    }

    /**
     * Busca reseñas por calificación mínima
     * @param calificacionMinima Calificación mínima (1-5)
     * @return Lista de reseñas con calificación igual o superior
     */
    public List<Reseña> buscarPorCalificacionMinima(int calificacionMinima) {
        return reseñas.stream()
                .filter(r -> r.getCalificacion() >= calificacionMinima)
                .collect(Collectors.toList());
    }

    /**
     * Busca reseñas verificadas
     * @return Lista de reseñas verificadas por administración
     */
    public List<Reseña> buscarVerificadas() {
        return reseñas.stream()
                .filter(Reseña::isVerificada)
                .collect(Collectors.toList());
    }

    /**
     * Busca reseñas por tipo de calificación
     * @param tipo Tipo de calificación (ESTANDAR, DETALLADA, PREMIUM)
     * @return Lista de reseñas del tipo especificado
     */
    public List<Reseña> buscarPorTipo(TipoCalificacion tipo) {
        return reseñas.stream()
                .filter(r -> r.getTipoCalificacion() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el promedio de calificaciones de un alojamiento
     * @param alojamientoId ID del alojamiento
     * @return Promedio de calificaciones (0 si no hay reseñas)
     */
    public double calcularPromedioAlojamiento(String alojamientoId) {
        return buscarPorAlojamiento(alojamientoId).stream()
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0.0);
    }

    /**
     * Obtiene reseñas destacadas (altas calificaciones con fotos)
     * @return Lista de reseñas destacadas
     */
    public List<Reseña> buscarDestacadas() {
        return reseñas.stream()
                .filter(Reseña::esDestacada)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una reseña
     * @param id ID de la reseña a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarReseña(String id) {
        return reseñas.removeIf(r -> r.getId().equals(id));
    }

    /**
     * Actualiza una reseña existente
     * @param reseñaActualizada Reseña con datos actualizados
     * @throws IllegalArgumentException si la reseña no existe
     */
    public void actualizarReseña(Reseña reseñaActualizada) {
        Reseña existente = buscarPorId(reseñaActualizada.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));

        int index = reseñas.indexOf(existente);
        reseñas.set(index, reseñaActualizada);
    }

    /**
     * Verifica si existe una reseña con el ID
     * @param id ID a verificar
     * @return true si existe, false si no
     */
    public boolean existeReseña(String id) {
        return reseñas.stream()
                .anyMatch(r -> r.getId().equals(id));
    }

    /**
     * Cuenta el total de reseñas
     * @return Número total de reseñas
     */
    public long contarReseñas() {
        return reseñas.size();
    }

    /**
     * Busca reseñas que contengan texto específico
     * @param texto Texto a buscar en comentarios
     * @return Lista de reseñas que contienen el texto
     */
    public List<Reseña> buscarPorTexto(String texto) {
        return reseñas.stream()
                .filter(r -> r.getComentario().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las últimas reseñas añadidas
     * @param limite Cantidad máxima de reseñas a devolver
     * @return Lista de las últimas reseñas
     */
    public List<Reseña> obtenerUltimasReseñas(int limite) {
        return reseñas.stream()
                .sorted((r1, r2) -> r2.getFecha().compareTo(r1.getFecha()))
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Guarda una reseña en el repositorio
     * @param reseña La reseña a guardar
     * @throws IllegalArgumentException si la reseña es nula o ya existe
     */
    public void guardar(Reseña reseña) {  // Cambiado de guardarResena a guardar
        if (reseña == null) {
            throw new IllegalArgumentException("La reseña no puede ser nula");
        }
        if (existeReseña(reseña.getId())) {
            throw new IllegalArgumentException("Ya existe una reseña con el ID: " + reseña.getId());
        }
        reseñas.add(reseña);
    }
}