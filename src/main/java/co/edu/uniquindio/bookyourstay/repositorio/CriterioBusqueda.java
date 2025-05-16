package co.edu.uniquindio.bookyourstay.repositorio;

/**
 * Interfaz funcional para definir criterios de búsqueda personalizados
 * @param <T> Tipo de entidad sobre la que se aplicará el criterio
 */
@FunctionalInterface
public interface CriterioBusqueda<T> {
    /**
     * Evalúa si un objeto cumple con el criterio de búsqueda
     * @param objeto El objeto a evaluar
     * @return true si el objeto cumple con el criterio
     */
    boolean cumpleCriterio(T objeto);
}