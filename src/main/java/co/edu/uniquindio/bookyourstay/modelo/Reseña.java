package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.EstadoReserva;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCalificacion;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

@AllArgsConstructor
@Builder
public class Reseña {
    private String id;
    private Cliente cliente;
    private Alojamiento alojamiento;
    private Reserva reserva;
    private int calificacion; // 1-5
    private String comentario;
    private LocalDateTime fecha;
    private boolean verificada;
    private List<String> fotos;
    private TipoCalificacion tipoCalificacion;
    private boolean recomendaria;
    private String respuestaAdministrador;
    private LocalDateTime fechaRespuesta;

    public Reseña() {
        this.id = "RESEÑA-" + UUID.randomUUID().toString().substring(0, 8);
        this.fecha = LocalDateTime.now();
        this.verificada = false;
        this.fotos = new ArrayList<>();
        this.tipoCalificacion = TipoCalificacion.ESTANDAR;
    }

    /**
     * Valida que la reseña cumpla con todos los requisitos
     */
    public boolean esValida() {
        return calificacion >= 1 && calificacion <= 5 &&
                comentario != null && !comentario.isBlank() &&
                comentario.length() >= 20 && comentario.length() <= 500 &&
                reserva != null && reserva.getEstado() == EstadoReserva.COMPLETADA &&
                alojamiento != null && cliente != null;
    }

    /**
     * Verifica la reseña (solo administradores)
     */
    public void verificarReseña(String respuestaAdmin) {
        if (respuestaAdmin == null || respuestaAdmin.isBlank()) {
            throw new IllegalArgumentException("La respuesta del administrador es requerida");
        }
        this.verificada = true;
        this.respuestaAdministrador = respuestaAdmin;
        this.fechaRespuesta = LocalDateTime.now();
    }

    /**
     * Edita el comentario de la reseña (solo si no está verificada)
     */
    public void editarComentario(String nuevoComentario) {
        if (verificada) {
            throw new IllegalStateException("No se puede editar una reseña verificada");
        }
        if (nuevoComentario == null || nuevoComentario.isBlank()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }
        this.comentario = nuevoComentario;
    }

    /**
     * Agrega fotos a la reseña
     */
    public void agregarFoto(String urlFoto) {
        if (fotos.size() >= 5) {
            throw new IllegalStateException("Máximo 5 fotos por reseña");
        }
        if (urlFoto == null || urlFoto.isBlank()) {
            throw new IllegalArgumentException("La URL de la foto no puede estar vacía");
        }
        fotos.add(urlFoto);
    }

    /**
     * Calcula si la reseña es destacada
     */
    public boolean esDestacada() {
        return calificacion >= 4 &&
                comentario.length() >= 100 &&
                fotos.size() >= 2 &&
                tipoCalificacion == TipoCalificacion.DETALLADA;
    }

    /**
     * Genera un reporte completo de la reseña
     */
    public String generarReporte() {
        return String.format(
                "Reseña #%s\n" +
                        "Alojamiento: %s\n" +
                        "Cliente: %s\n" +
                        "Fecha estadía: %s a %s\n" +
                        "Calificación: %d/5\n" +
                        "Tipo: %s\n" +
                        "Recomendaría: %s\n" +
                        "Estado: %s\n" +
                        "Fotos: %d\n\n" +
                        "Comentario:\n%s\n\n" +
                        "%s",
                id,
                alojamiento.getNombre(),
                cliente.getNombre(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                calificacion,
                tipoCalificacion,
                recomendaria ? "Sí" : "No",
                verificada ? "Verificada" : "No verificada",
                fotos.size(),
                comentario,
                verificada ? "Respuesta del administrador (" + fechaRespuesta + "):\n" + respuestaAdministrador : ""
        );
    }
    public Reseña(String nombreCliente, String nombreAlojamiento, String comentario, int calificacion) {
        this();
        this.calificacion = calificacion;
        this.comentario = comentario;

        // Crear cliente con nombre
        this.cliente = new co.edu.uniquindio.bookyourstay.modelo.Cliente();
        this.cliente.setNombre(nombreCliente);

        // Crear alojamiento concreto (ejemplo Casa)
        this.alojamiento = new co.edu.uniquindio.bookyourstay.modelo.Casa();
        this.alojamiento.setNombre(nombreAlojamiento);
    }
    @Override
    public String toString() {
        return String.format("Reseña de %s - %d/5 - %s",
                alojamiento.getNombre(),
                calificacion,
                fecha.toLocalDate());
    }
}