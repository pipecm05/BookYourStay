package co.edu.uniquindio.bookyourstay.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private Billetera billetera;
    private List<Reserva> reservas;
    private List<Reseña> reseñas;

    public Cliente(String cedula, String nombre, String telefono,
                   String email, String password) {
        super(cedula, nombre, telefono, email, password);
        this.billetera = new Billetera();
        this.reservas = new ArrayList<>();
        this.reseñas = new ArrayList<>();
    }

    // Getters específicos de Cliente
    public Billetera getBilletera() { return billetera; }
    public List<Reserva> getReservas() { return reservas; }
    public List<Reseña> getResenas() { return reseñas; }

    // Métodos para manejar reservas
    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public void cancelarReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    // Métodos para manejar reseñas
    public void agregarReseña(Reseña reseña) {
        reseñas.add(reseña);
    }
}