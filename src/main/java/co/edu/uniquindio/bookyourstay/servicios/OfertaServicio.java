package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Oferta;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoOferta;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoOferta;
import co.edu.uniquindio.bookyourstay.repositorios.OfertaRepositorio;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OfertaServicio {
    private final OfertaRepositorio ofertaRepositorio;

    public OfertaServicio(OfertaRepositorio ofertaRepositorio) {
        this.ofertaRepositorio = ofertaRepositorio;
    }

    /**
     * Crea una nueva oferta en el sistema
     * @param nombre Nombre de la oferta
     * @param descripcion Descripción detallada
     * @param tipo Tipo de oferta (PORCENTAJE, MONTO_FIJO)
     * @param valor Valor del descuento
     * @param fechaInicio Fecha de inicio de vigencia
     * @param fechaFin Fecha de fin de vigencia
     * @param alojamientos Lista de alojamientos aplicables
     * @return Oferta creada
     * @throws IllegalArgumentException Si los datos no son válidos o ya existe una oferta con el mismo nombre
     */
    public Oferta crearOferta(String nombre, String descripcion, TipoOferta tipo,
                              float valor, LocalDate fechaInicio, LocalDate fechaFin,
                              List<Alojamiento> alojamientos) throws IllegalArgumentException {

        validarDatosOferta(nombre, descripcion, valor, fechaInicio, fechaFin);

        if (existeOfertaConNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una oferta con el nombre: " + nombre);
        }

        Oferta oferta = Oferta.builder()
                .id(generarId())
                .nombre(nombre)
                .descripcion(descripcion)
                .tipoOferta(tipo)
                .valor(valor)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .activa(true)
                .estado(EstadoOferta.ACTIVA)
                .alojamientosAplicables(alojamientos)
                .maximoUsos(0) // 0 = ilimitado
                .usosActuales(0)
                .codigoPromocional(generarCodigoPromocional(nombre))
                .build();

        ofertaRepositorio.guardarOferta(oferta);
        return oferta;
    }

    /**
     * Actualiza una oferta existente
     * @param id ID de la oferta a actualizar
     * @param nuevosDatos Oferta con los nuevos datos
     * @return Oferta actualizada
     * @throws NoSuchElementException Si no se encuentra la oferta
     * @throws IllegalArgumentException Si los nuevos datos no son válidos
     */
    public Oferta actualizarOferta(String id, Oferta nuevosDatos) throws NoSuchElementException, IllegalArgumentException {
        validarDatosOferta(nuevosDatos.getNombre(), nuevosDatos.getDescripcion(),
                nuevosDatos.getValor(), nuevosDatos.getFechaInicio(), nuevosDatos.getFechaFin());

        Oferta oferta = obtenerOferta(id);
        oferta.setNombre(nuevosDatos.getNombre());
        oferta.setDescripcion(nuevosDatos.getDescripcion());
        oferta.setTipoOferta(nuevosDatos.getTipoOferta());
        oferta.setValor(nuevosDatos.getValor());
        oferta.setFechaInicio(nuevosDatos.getFechaInicio());
        oferta.setFechaFin(nuevosDatos.getFechaFin());
        oferta.setAlojamientosAplicables(nuevosDatos.getAlojamientosAplicables());

        ofertaRepositorio.actualizarOferta(oferta);
        return oferta;
    }

    /**
     * Cambia el estado de una oferta
     * @param id ID de la oferta
     * @param estado Nuevo estado
     * @return Oferta actualizada
     * @throws NoSuchElementException Si no se encuentra la oferta
     */
    public Oferta cambiarEstadoOferta(String id, EstadoOferta estado) throws NoSuchElementException {
        Oferta oferta = obtenerOferta(id);
        oferta.setEstado(estado);
        oferta.setActiva(estado == EstadoOferta.ACTIVA);
        ofertaRepositorio.actualizarOferta(oferta);
        return oferta;
    }

    /**
     * Obtiene una oferta por su ID
     * @param id ID de la oferta
     * @return Oferta encontrada
     * @throws NoSuchElementException Si no se encuentra la oferta
     */
    public Oferta obtenerOferta(String id) throws NoSuchElementException {
        return ofertaRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada con ID: " + id));
    }

    /**
     * Aplica una oferta a un precio base
     * @param ofertaId ID de la oferta
     * @param precioBase Precio original
     * @return Precio con descuento aplicado
     * @throws NoSuchElementException Si no se encuentra la oferta
     * @throws IllegalStateException Si la oferta no es aplicable
     */
    public float aplicarOferta(String ofertaId, float precioBase) throws NoSuchElementException, IllegalStateException {
        Oferta oferta = obtenerOferta(ofertaId);

        if (!oferta.esVigente()) {
            throw new IllegalStateException("La oferta no está vigente");
        }

        if (oferta.getMaximoUsos() > 0 && oferta.getUsosActuales() >= oferta.getMaximoUsos()) {
            throw new IllegalStateException("La oferta ha alcanzado su límite de usos");
        }

        float precioFinal = oferta.getTipoOferta() == TipoOferta.PORCENTAJE ?
                precioBase * (1 - oferta.getValor() / 100) :
                Math.max(0, precioBase - oferta.getValor());

        // Registrar uso
        oferta.setUsosActuales(oferta.getUsosActuales() + 1);
        ofertaRepositorio.actualizarOferta(oferta);

        return precioFinal;
    }

    // Métodos que no lanzan excepciones (mantienen su implementación original)
    public List<Oferta> listarOfertasVigentes() {
        return ofertaRepositorio.listarTodas().stream()
                .filter(Oferta::esVigente)
                .collect(Collectors.toList());
    }

    public List<Oferta> listarOfertasParaAlojamiento(String alojamientoId) {
        return ofertaRepositorio.listarTodas().stream()
                .filter(Oferta::esVigente)
                .filter(o -> o.getAlojamientosAplicables().stream()
                        .anyMatch(a -> a.getId().equals(alojamientoId)))
                .collect(Collectors.toList());
    }

    public Map<String, Object> generarReporteOfertas() {
        List<Oferta> todas = ofertaRepositorio.listarTodas();

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("totalOfertas", todas.size());
        reporte.put("ofertasActivas", todas.stream().filter(Oferta::isActiva).count());
        reporte.put("ofertasMasUsadas", todas.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsosActuales(), o1.getUsosActuales()))
                .limit(5)
                .collect(Collectors.toList()));
        Object efectividadPorTipo = reporte.put("efectividadPorTipo", todas.stream()
                .collect(Collectors.groupingBy(
                        Oferta::getTipoOferta,
                        Collectors.averagingInt(Oferta::getUsosActuales)
                )));

        return reporte;
    }

    private String generarId() {
        return "OF-" + System.currentTimeMillis();
    }

    private String generarCodigoPromocional(String nombre) {
        String codigo = nombre.substring(0, Math.min(3, nombre.length())).toUpperCase();
        return codigo + "-" + (int)(Math.random() * 1000);
    }

    private boolean existeOfertaConNombre(String nombre) {
        return ofertaRepositorio.listarTodas().stream()
                .anyMatch(o -> o.getNombre().equalsIgnoreCase(nombre));
    }

    private void validarDatosOferta(String nombre, String descripcion, float valor,
                                    LocalDate fechaInicio, LocalDate fechaFin) throws IllegalArgumentException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la oferta es requerido");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es requerida");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor a cero");
        }
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("Las fechas de la oferta no son válidas");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado");
        }
    }
}