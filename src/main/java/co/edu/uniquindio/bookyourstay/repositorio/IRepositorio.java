package co.edu.uniquindio.bookyourstay.repositorio;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones básicas de repositorio
 * @param <T> Tipo de entidad que manejará el repositorio
 */
public interface IRepositorio<T> {

    /**
     * Guarda una entidad en el repositorio
     * @param objeto Entidad a guardar
     * @return Entidad guardada
     */
    T guardar(T objeto);

    /**
     * Busca una entidad por su identificador único
     * @param id Identificador de la entidad
     * @return Optional que contiene la entidad si existe
     */
    Optional<T> buscarPorId(String id);

    /**
     * Obtiene todas las entidades del repositorio
     * @return Lista de todas las entidades
     */
    List<T> listarTodos();

    /**
     * Elimina una entidad del repositorio
     * @param id Identificador de la entidad a eliminar
     * @return true si la eliminación fue exitosa
     */
    boolean eliminar(String id);

    /**
     * Actualiza una entidad existente
     * @param objeto Entidad con los datos actualizados
     * @return Entidad actualizada
     */
    T actualizar(T objeto);

    /**
     * Busca entidades que coincidan con un criterio específico
     * @param criterio Expresión lambda con el criterio de búsqueda
     * @return Lista de entidades que cumplen el criterio
     */
    List<T> buscarPorCriterio(CriterioBusqueda<T> criterio);

    /**
     * Verifica si existe una entidad con el identificador dado
     * @param id Identificador a verificar
     * @return true si existe una entidad con ese ID
     */
    boolean existe(String id);

    /**
     * Cuenta el número total de entidades en el repositorio
     * @return Cantidad total de entidades
     */
    long contar();

    /**
     * Busca la primera entidad que cumpla con el criterio dado
     * @param criterio Criterio de búsqueda
     * @return Optional con la entidad si se encuentra
     */
    Optional<T> buscarPrimeroQue(CriterioBusqueda<T> criterio);
}
