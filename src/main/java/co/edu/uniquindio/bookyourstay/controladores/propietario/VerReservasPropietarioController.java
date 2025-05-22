package co.edu.uniquindio.bookyourstay.controladores.propietario;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class VerReservasPropietarioController {

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> colAlojamiento;

    @FXML
    private TableColumn<Reserva, String> colCliente;

    @FXML
    private TableColumn<Reserva, String> colFechaInicio;

    @FXML
    private TableColumn<Reserva, String> colFechaFin;

    @FXML
    private TableColumn<Reserva, String> colEstado;

    private ObservableList<Reserva> reservas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurar columnas
        colAlojamiento.setCellValueFactory(data -> data.getValue().alojamientoProperty());
        colCliente.setCellValueFactory(data -> data.getValue().clienteProperty());
        colFechaInicio.setCellValueFactory(data -> data.getValue().fechaInicioProperty());
        colFechaFin.setCellValueFactory(data -> data.getValue().fechaFinProperty());
        colEstado.setCellValueFactory(data -> data.getValue().estadoProperty());

        // Agregar datos de ejemplo
        reservas.addAll(
                new Reserva("Casa Armenia", "Ana Pérez", "2025-07-01", "2025-07-05", "Confirmada"),
                new Reserva("Apto Salento", "Juan Gómez", "2025-06-20", "2025-06-25", "Pendiente")
        );

        tablaReservas.setItems(reservas);
    }

    // Clase interna temporal (reemplazar con la real del sistema)
    public static class Reserva {
        private final SimpleStringProperty alojamiento;
        private final SimpleStringProperty cliente;
        private final SimpleStringProperty fechaInicio;
        private final SimpleStringProperty fechaFin;
        private final SimpleStringProperty estado;

        public Reserva(String alojamiento, String cliente, String fechaInicio, String fechaFin, String estado) {
            this.alojamiento = new SimpleStringProperty(alojamiento);
            this.cliente = new SimpleStringProperty(cliente);
            this.fechaInicio = new SimpleStringProperty(fechaInicio);
            this.fechaFin = new SimpleStringProperty(fechaFin);
            this.estado = new SimpleStringProperty(estado);
        }

        public SimpleStringProperty alojamientoProperty() { return alojamiento; }
        public SimpleStringProperty clienteProperty() { return cliente; }
        public SimpleStringProperty fechaInicioProperty() { return fechaInicio; }
        public SimpleStringProperty fechaFinProperty() { return fechaFin; }
        public SimpleStringProperty estadoProperty() { return estado; }
    }
}