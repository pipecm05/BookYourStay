package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Apartamento;

import java.util.List;

public class ApartamentoFactory implements AlojamientoFactory {
    @Override
    public Alojamiento crearAlojamiento(String id, String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios) {
        Apartamento apartamento = new Apartamento();
        apartamento.setId(id);
        apartamento.setNombre(nombre);
        apartamento.setCiudad(ciudad);
        apartamento.setDescripcion(descripcion);
        apartamento.setPrecioNoche(precioNoche);
        apartamento.setCapacidadMax(capacidadMax);
        apartamento.setServicios(servicios);
        return apartamento;
    }
}