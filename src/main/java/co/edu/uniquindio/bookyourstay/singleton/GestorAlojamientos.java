package co.edu.uniquindio.bookyourstay.singleton;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorAlojamientos {
    private static GestorAlojamientos instancia;
    private final ObservableList<Alojamiento> alojamientos = FXCollections.observableArrayList();

    private GestorAlojamientos() {}

    public static synchronized GestorAlojamientos getInstancia() {
        if (instancia == null) {
            instancia = new GestorAlojamientos();
        }
        return instancia;
    }

    public void agregarAlojamiento(Alojamiento alojamiento) {
        alojamientos.add(alojamiento);
    }

    public ObservableList<Alojamiento> getAlojamientos() {
        return FXCollections.observableArrayList(alojamientos);
    }

    public List<Alojamiento> buscarPorCiudad(String ciudad) {
        return alojamientos.stream()
                .filter(a -> a.getCiudad().equalsIgnoreCase(ciudad))
                .collect(Collectors.toList());
    }
}