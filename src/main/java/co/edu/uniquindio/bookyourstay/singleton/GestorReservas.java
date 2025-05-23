package co.edu.uniquindio.bookyourstay.singleton;

import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorReservas {
    private static GestorReservas instancia;
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();

    private GestorReservas() {
        // Aquí podrías cargar reservas de una base de datos
    }

    public static synchronized GestorReservas getInstancia() {
        if (instancia == null) {
            instancia = new GestorReservas();
        }
        return instancia;
    }

    public ObservableList<Reserva> obtenerReservasPropietario() {
        // Filtrar reservas del propietario actual
        return FXCollections.observableArrayList(reservas);
    }

    public void cancelarReserva(Reserva reserva, String motivo) throws Exception {
        if (!reservas.contains(reserva)) {
            throw new Exception("Reserva no encontrada");
        }
        reserva.cancelar(motivo);
    }

    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public ObservableList<Reserva> obtenerReservasPorCiudad(String ciudad) {
        return reservas.stream()
                .filter(r -> r.getCiudadAlojamiento().equalsIgnoreCase(ciudad))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public ObservableList<Reserva> obtenerReservasPorPropietario(String idPropietario) {
        return reservas.stream()
                .filter(r -> r.getAlojamiento().getPropietario().getId().equals(idPropietario))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}