package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Hotel;

import java.util.List;

public class HotelFactory implements AlojamientoFactory {
    @Override
    public Alojamiento crearAlojamiento(String id, String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setNombre(nombre);
        hotel.setCiudad(ciudad);
        hotel.setDescripcion(descripcion);
        hotel.setPrecioNoche(precioNoche);
        hotel.setCapacidadMax(capacidadMax);
        hotel.setServicios(servicios);
        return hotel;
    }
}