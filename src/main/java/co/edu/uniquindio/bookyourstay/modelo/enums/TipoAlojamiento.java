package co.edu.uniquindio.bookyourstay.modelo.enums;

public enum TipoAlojamiento {
    CASA,
    APARTAMENTO,
    HOTEL;

    // Método nuevo para convertir nombres de clase a enum
    public static TipoAlojamiento fromClass(Class<?> clazz) {
        String simpleName = clazz.getSimpleName().toUpperCase();
        try {
            return TipoAlojamiento.valueOf(simpleName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de alojamiento no soportado: " + simpleName);
        }
    }
}