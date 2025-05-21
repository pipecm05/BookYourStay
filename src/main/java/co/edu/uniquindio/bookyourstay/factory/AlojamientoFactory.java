package co.edu.uniquindio.bookyourstay.factory;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import java.util.List;

public interface AlojamientoFactory {
    Alojamiento crearAlojamiento(String id, String nombre, String ciudad, String descripcion,
                                 float precioNoche, int capacidadMax, List<String> servicios);
}