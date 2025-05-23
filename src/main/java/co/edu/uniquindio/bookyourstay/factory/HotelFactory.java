package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Hotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class HotelFactory implements AlojamientoFactory {
    @Override
    public Alojamiento crearAlojamiento(String id, String nombre, String ciudad,
                                        String descripcion, float precioNoche,
                                        int capacidadMax, List<String> servicios) {
        Hotel hotel = new Hotel();

        // Setters heredados de Alojamiento
        hotel.setId(id);
        hotel.setNombre(nombre);
        hotel.setCiudad(ciudad);  // Asegúrate que este setter existe
        hotel.setDescripcion(descripcion);
        hotel.setPrecioNoche(precioNoche);
        hotel.setCapacidadMax(capacidadMax);

        // Para servicios (ObservableList)
        hotel.getServicios().setAll(FXCollections.observableArrayList(servicios));

        return hotel;
    }
}