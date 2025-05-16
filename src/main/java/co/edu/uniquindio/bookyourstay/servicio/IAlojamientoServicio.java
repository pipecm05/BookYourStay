package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IAlojamientoServicio {
    Alojamiento crearAlojamiento(String tipo, String nombre, String ciudad,
                                 String descripcion, String imagen,
                                 double precioNoche, int capacidad,
                                 double costoAseo) throws Exception;
    boolean actualizarAlojamiento(Alojamiento alojamiento) throws Exception;
    boolean eliminarAlojamiento(String id) throws Exception;
    Optional<Alojamiento> buscarAlojamiento(String id);
    List<Alojamiento> listarAlojamientos();
    List<Alojamiento> buscarPorCiudad(String ciudad);
    List<Alojamiento> buscarPorNombre(String nombre);
    List<Alojamiento> buscarPorRangoPrecio(double min, double max);
    List<Alojamiento> buscarPorTipo(String tipo);
    List<String> listarCiudades();
    boolean agregarOferta(String idAlojamiento, Oferta oferta) throws Exception;
    List<Oferta> obtenerOfertasVigentes(LocalDate fecha);
    List<Alojamiento> obtenerAlojamientosPopulares();
    List<Alojamiento> obtenerAlojamientosRecomendados(String idCliente);
}