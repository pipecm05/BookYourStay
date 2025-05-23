package co.edu.uniquindio.bookyourstay.factory;


import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Apartamento;
import java.util.List;

public class ApartamentoFactory implements AlojamientoFactory {
    @Override
    public Alojamiento crearAlojamiento(String id, String nombre, String ciudad, String descripcion,
                                        float precioNoche, int capacidadMax, List<String> servicios) {

        // Usa el constructor con parámetros
        Apartamento apartamento = new Apartamento(nombre, ciudad, descripcion, capacidadMax, precioNoche);

        // Establece propiedades adicionales
        apartamento.setId(id);
        apartamento.setServicios(servicios);

        return apartamento;
    }
}