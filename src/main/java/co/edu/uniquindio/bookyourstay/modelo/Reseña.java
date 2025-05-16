package co.edu.uniquindio.bookyourstay.modelo;

import java.time.LocalDate;

public class Reseña {
    private Cliente cliente;
    private Alojamiento alojamiento;
    private String comentario;
    private int calificacion; // 1-5
    private LocalDate fecha;

    public Reseña(Cliente cliente, Alojamiento alojamiento,
                  String comentario, int calificacion) {
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = LocalDate.now();
    }

    // Getters
    public Cliente getCliente() { return cliente; }
    public Alojamiento getAlojamiento() { return alojamiento; }
    public String getComentario() { return comentario; }
    public int getCalificacion() { return calificacion; }
    public LocalDate getFecha() { return fecha; }
}