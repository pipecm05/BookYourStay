package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Casa;
import java.util.List;

public class CasaFactory implements AlojamientoFactory {
    @Override
    public Alojamiento crearAlojamiento(String id, String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios) {

        // Crea la Casa con el constructor de 5 parámetros
        Casa casa = new Casa(nombre, ciudad, descripcion, capacidadMax, precioNoche);

        // Establece los demás valores
        casa.setId(id);
        casa.setServicios(servicios);

        return casa;
    }
}