package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCalificacion;
import co.edu.uniquindio.bookyourstay.repositorios.ReseñaRepositorio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ReseñaServicio {

    private static ReseñaServicio instancia;

    private final ReseñaRepositorio reseñaRepositorio;

    // ObservableList para UI (JavaFX)
    private final ObservableList<Reseña> listaReseñas;

    // Constructor privado para singleton
    private ReseñaServicio() {
        this.reseñaRepositorio = new ReseñaRepositorio();
        // Inicializar la lista observable con todas las reseñas actuales
        this.listaReseñas = FXCollections.observableArrayList(reseñaRepositorio.listarTodas());
    }

    // Método para obtener la instancia singleton
    public static synchronized ReseñaServicio obtenerInstancia() {
        if (instancia == null) {
            instancia = new ReseñaServicio();
        }
        return instancia;
    }

    // Método para obtener la lista observable
    public ObservableList<Reseña> getListaReseñas() {
        return listaReseñas;
    }

    public Reseña crearReseña(Cliente cliente, Alojamiento alojamiento, Reserva reserva,
                              int calificacion, String comentario, TipoCalificacion tipo,
                              boolean recomendaria) {
        validarReserva(reserva);
        validarCalificacion(calificacion);
        validarComentario(comentario);
        verificarReseñaExistente(reserva);

        Reseña reseña = construirReseña(cliente, alojamiento, reserva, calificacion, comentario, tipo, recomendaria);

        reseñaRepositorio.guardar(reseña);
        listaReseñas.add(reseña); // Actualizar lista observable
        return reseña;
    }

    public Reseña verificarReseña(String reseñaId, String respuestaAdmin) throws NoSuchElementException {
        Reseña reseña = obtenerReseña(reseñaId);
        reseña.setVerificada(true);
        reseña.setRespuestaAdministrador(respuestaAdmin);
        reseña.setFechaRespuesta(LocalDateTime.now());
        reseñaRepositorio.actualizarReseña(reseña);
        // Actualizar lista observable: quitar y volver a agregar para refrescar binding si aplica
        listaReseñas.removeIf(r -> r.getId().equals(reseñaId));
        listaReseñas.add(reseña);
        return reseña;
    }

    public List<Reseña> listarResenasPorAlojamiento(String alojamientoId) {
        return reseñaRepositorio.buscarPorAlojamiento(alojamientoId);
    }

    public List<Reseña> listarResenasVerificadasPorAlojamiento(String alojamientoId) {
        return reseñaRepositorio.buscarPorAlojamiento(alojamientoId).stream()
                .filter(Reseña::isVerificada)
                .collect(Collectors.toList());
    }

    public float calcularPromedioCalificaciones(String alojamientoId) {
        List<Reseña> reseñas = listarResenasVerificadasPorAlojamiento(alojamientoId);
        if (reseñas.isEmpty()) return 0;

        return (float) reseñas.stream()
                .mapToInt(Reseña::getCalificacion)
                .average()
                .orElse(0);
    }

    public List<Reseña> obtenerResenasDestacadas(String alojamientoId) {
        List<Reseña> reseñas = alojamientoId != null ?
                reseñaRepositorio.buscarPorAlojamiento(alojamientoId) :
                reseñaRepositorio.listarTodas();

        return reseñas.stream()
                .filter(Reseña::esDestacada)
                .collect(Collectors.toList());
    }

    public Reseña obtenerReseña(String reseñaId) throws NoSuchElementException {
        return reseñaRepositorio.buscarPorId(reseñaId)
                .orElseThrow(() -> new NoSuchElementException("Reseña no encontrada con ID: " + reseñaId));
    }

    private Reseña construirReseña(Cliente cliente, Alojamiento alojamiento, Reserva reserva,
                                   int calificacion, String comentario, TipoCalificacion tipo,
                                   boolean recomendaria) {
        return Reseña.builder()
                .id(generarId())
                .cliente(cliente)
                .alojamiento(alojamiento)
                .reserva(reserva)
                .calificacion(calificacion)
                .comentario(comentario.trim())
                .fecha(LocalDateTime.now())
                .verificada(false)
                .tipoCalificacion(tipo)
                .recomendaria(recomendaria)
                .build();
    }

    private void validarReserva(Reserva reserva) throws IllegalStateException {
        if (reserva.getEstado() != EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("Solo puede dejar reseña en reservas completadas");
        }
    }

    private void validarCalificacion(int calificacion) throws IllegalArgumentException {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }
    }

    private void validarComentario(String comentario) throws IllegalArgumentException {
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }
        if (comentario.length() < 20 || comentario.length() > 500) {
            throw new IllegalArgumentException("El comentario debe tener entre 20 y 500 caracteres");
        }
    }

    private void verificarReseñaExistente(Reserva reserva) throws IllegalStateException {
        if (reseñaRepositorio.buscarPorReserva(reserva.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe una reseña para esta reserva");
        }
    }

    private String generarId() {
        return "RESE-" + System.currentTimeMillis();
    }
}