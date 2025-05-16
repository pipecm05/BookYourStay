package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.repositorio.AlojamientoRepositorio;
import co.edu.uniquindio.bookyourstay.repositorio.ReservaRepositorio;
import co.edu.uniquindio.bookyourstay.repositorio.ReseñaRepositorio;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AlojamientoServicio implements IAlojamientoServicio {
    private static AlojamientoServicio instancia;
    private final AlojamientoRepositorio alojamientoRepositorio;
    private final ReservaRepositorio reservaRepositorio;
    private final ReseñaRepositorio resenaRepositorio;

    private AlojamientoServicio() {
        this.alojamientoRepositorio = AlojamientoRepositorio.getInstancia();
        this.reservaRepositorio = ReservaRepositorio.getInstancia();
        this.resenaRepositorio = ReseñaRepositorio.getInstancia();
    }

    public static AlojamientoServicio getInstancia() {
        if (instancia == null) {
            instancia = new AlojamientoServicio();
        }
        return instancia;
    }

    @Override
    public Alojamiento crearAlojamiento(String tipo, String nombre, String ciudad,
                                        String descripcion, String imagen,
                                        double precioNoche, int capacidad,
                                        double costoAseo) throws Exception {
        validarCampos(tipo, nombre, ciudad, descripcion, imagen);

        if (precioNoche <= 0 || capacidad <= 0) {
            throw new Exception("Precio y capacidad deben ser positivos");
        }

        // Validar tipo de alojamiento
        if (!Arrays.asList("casa", "apartamento", "hotel").contains(tipo.toLowerCase())) {
            throw new Exception("Tipo de alojamiento no válido. Debe ser: Casa, Apartamento o Hotel");
        }

        // Validar costo de aseo para tipos que lo requieren
        if (("casa".equalsIgnoreCase(tipo) || "apartamento".equalsIgnoreCase(tipo)) && costoAseo <= 0) {
            throw new Exception("El costo de aseo debe ser positivo para casas y apartamentos");
        }

        // Crear el alojamiento usando el factory method
        Alojamiento alojamiento = Alojamiento.crearAlojamiento(
                tipo, nombre, ciudad, descripcion, imagen, precioNoche, capacidad);

        // Configurar propiedades específicas
        if (alojamiento instanceof Casa) {
            ((Casa) alojamiento).setCostoAseo(costoAseo);
        } else if (alojamiento instanceof Apartamento) {
            ((Apartamento) alojamiento).setCostoAseo(costoAseo);
        }

        // Configurar servicios básicos
        List<String> serviciosBasicos = Arrays.asList("Wifi", "Agua caliente", "Aseo básico");
        alojamiento.getServicios().addAll(serviciosBasicos);

        return alojamientoRepositorio.guardar(alojamiento);
    }
    @Override
    public boolean actualizarAlojamiento(Alojamiento alojamiento) throws Exception {
        if (alojamiento == null) {
            throw new Exception("El alojamiento no puede ser nulo");
        }

        validarCampos(
                alojamiento.getClass().getSimpleName(),
                alojamiento.getNombre(),
                alojamiento.getCiudad(),
                alojamiento.getDescripcion(),
                alojamiento.getImagen()
        );

        if (alojamiento.getPrecioNoche() <= 0 || alojamiento.getCapacidad() <= 0) {
            throw new Exception("Precio y capacidad deben ser positivos");
        }

        return alojamientoRepositorio.actualizar(alojamiento) != null;
    }

    @Override
    public boolean eliminarAlojamiento(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El ID del alojamiento no puede estar vacío");
        }

        // Verificar si hay reservas futuras para este alojamiento
        LocalDate hoy = LocalDate.now();
        boolean tieneReservasFuturas = reservaRepositorio.listarTodos().stream()
                .filter(r -> r.getAlojamiento().getId().equals(id))
                .anyMatch(r -> !r.getFechaFin().isBefore(hoy));

        if (tieneReservasFuturas) {
            throw new Exception("No se puede eliminar un alojamiento con reservas futuras");
        }

        return alojamientoRepositorio.eliminar(id);
    }

    @Override
    public Optional<Alojamiento> buscarAlojamiento(String id) {
        return alojamientoRepositorio.buscarPorId(id);
    }

    @Override
    public List<Alojamiento> listarAlojamientos() {
        return alojamientoRepositorio.listarTodos();
    }

    @Override
    public List<Alojamiento> buscarPorCiudad(String ciudad) {
        return alojamientoRepositorio.buscarPorCiudad(ciudad);
    }

    @Override
    public List<Alojamiento> buscarPorNombre(String nombre) {
        return alojamientoRepositorio.buscarPorNombre(nombre);
    }

    @Override
    public List<Alojamiento> buscarPorRangoPrecio(double min, double max) {
        // Hacer copias efectivamente finales de los parámetros
        final double precioMin = min < max ? min : max;
        final double precioMax = min < max ? max : min;

        return alojamientoRepositorio.listarTodos().stream()
                .filter(a -> a.getPrecioNoche() >= precioMin && a.getPrecioNoche() <= precioMax)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alojamiento> buscarPorTipo(String tipo) {
        return alojamientoRepositorio.listarTodos().stream()
                .filter(a -> a.getClass().getSimpleName().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> listarCiudades() {
        return alojamientoRepositorio.obtenerCiudades();
    }

    @Override
    public boolean agregarOferta(String idAlojamiento, Oferta oferta) throws Exception {
        Optional<Alojamiento> optional = alojamientoRepositorio.buscarPorId(idAlojamiento);

        if (optional.isEmpty()) {
            throw new Exception("Alojamiento no encontrado");
        }

        if (oferta.getFechaInicio().isAfter(oferta.getFechaFin())) {
            throw new Exception("La fecha de inicio debe ser anterior a la fecha fin");
        }

        if (oferta.getDescuento() <= 0 || oferta.getDescuento() >= 1) {
            throw new Exception("El descuento debe estar entre 0 y 1 (0% a 100%)");
        }

        Alojamiento alojamiento = optional.get();
        alojamiento.getOfertas().add(oferta);

        return alojamientoRepositorio.actualizar(alojamiento) != null;
    }

    @Override
    public List<Oferta> obtenerOfertasVigentes(LocalDate fecha) {
        return alojamientoRepositorio.listarTodos().stream()
                .flatMap(a -> a.getOfertas().stream())
                .filter(o -> !fecha.isBefore(o.getFechaInicio()) && !fecha.isAfter(o.getFechaFin()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Alojamiento> obtenerAlojamientosPopulares() {
        return alojamientoRepositorio.listarTodos().stream()
                .sorted((a1, a2) -> Integer.compare(
                        reservaRepositorio.buscarPorAlojamiento(a2.getId()).size(),
                        reservaRepositorio.buscarPorAlojamiento(a1.getId()).size()
                ))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alojamiento> obtenerAlojamientosRecomendados(String idCliente) {
        // Implementación mejorada del algoritmo de recomendación
        return alojamientoRepositorio.listarTodos().stream()
                .sorted((a1, a2) -> Double.compare(
                        calcularPuntuacionRecomendacion(a2, idCliente),
                        calcularPuntuacionRecomendacion(a1, idCliente)
                ))
                .limit(5)
                .collect(Collectors.toList());
    }

    private double calcularPuntuacionRecomendacion(Alojamiento alojamiento, String idCliente) {
        double puntuacion = 0.0;

        // 1. Calificación promedio del alojamiento (40% peso)
        List<Reseña> resenas = resenaRepositorio.buscarPorAlojamiento(alojamiento.getId());
        if (!resenas.isEmpty()) {
            puntuacion += resenas.stream()
                    .mapToInt(Reseña::getCalificacion)
                    .average()
                    .orElse(0.0) * 0.4;
        }

        // 2. Popularidad (número de reservas) (30% peso)
        puntuacion += 0.3 * Math.log(1 + reservaRepositorio.buscarPorAlojamiento(alojamiento.getId()).size());

        // 3. Similitud con preferencias del cliente (30% peso)
        puntuacion += 0.3 * calcularSimilitudPreferencias(alojamiento, idCliente);

        return puntuacion;
    }

    private double calcularSimilitudPreferencias(Alojamiento alojamiento, String idCliente) {
        // Obtener historial del cliente
        List<Reserva> reservasCliente = reservaRepositorio.buscarPorCliente(idCliente);
        List<Reseña> resenasCliente = resenaRepositorio.buscarPorCliente(idCliente);

        double similitud = 0.0;

        // Preferencia por ciudad
        long countCiudadesVisitadas = reservasCliente.stream()
                .map(r -> r.getAlojamiento().getCiudad())
                .filter(ciudad -> ciudad.equals(alojamiento.getCiudad()))
                .count();
        similitud += 0.4 * (countCiudadesVisitadas > 0 ? 1.0 : 0.0);

        // Preferencia por tipo de alojamiento
        long countTipoPreferido = reservasCliente.stream()
                .map(r -> r.getAlojamiento().getClass().getSimpleName())
                .filter(tipo -> tipo.equals(alojamiento.getClass().getSimpleName()))
                .count();
        similitud += 0.3 * (countTipoPreferido > 0 ? 1.0 : 0.0);

        // Preferencia por servicios (simplificado)
        similitud += 0.3 * alojamiento.getServicios().stream()
                .filter(s -> s.contains("Wifi") || s.contains("Piscina")) // Servicios populares
                .count() * 0.1;

        return similitud;
    }

    private void validarCampos(String... campos) throws Exception {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                throw new Exception("Todos los campos son obligatorios");
            }
        }
    }
}