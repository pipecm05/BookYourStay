package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Casa;

import java.util.List;

public class CasaFactory implements AlojamientoFactory {
    @Override
    public Alojamiento crearAlojamiento(String id, String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios) {
        Casa casa = new Casa();
        casa.setId(id);
        casa.setNombre(nombre);
        casa.setCiudad(ciudad);
        casa.setDescripcion(descripcion);
        casa.setPrecioNoche(precioNoche);
        casa.setCapacidadMax(capacidadMax);
        casa.setServicios(servicios);
        return casa;
    }
}