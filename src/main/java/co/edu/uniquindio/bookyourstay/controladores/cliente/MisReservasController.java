package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MisReservasController {

    @FXML
    private TableView<ReservaDTO> tablaReservas;

    @FXML
    private TableColumn<ReservaDTO, String> colAlojamiento;

    @FXML
    private TableColumn<ReservaDTO, String> colFechaInicio;

    @FXML
    private TableColumn<ReservaDTO, String> colFechaFin;

    @FXML
    private TableColumn<ReservaDTO, String> colEstado;

    @FXML
    private Button btnVerFactura;

    @FXML
    private Label lblInfo;

    // Lista observable para las reservas
    private ObservableList<ReservaDTO> listaReservas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colAlojamiento.setCellValueFactory(new PropertyValueFactory<>("nombreAlojamiento"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Aquí debes cargar tus datos reales en listaReservas
        // Ejemplo datos de prueba:
        listaReservas.add(new ReservaDTO("Hotel Quindio", "2025-06-01", "2025-06-10", "Confirmada"));
        listaReservas.add(new ReservaDTO("Casa del Lago", "2025-07-15", "2025-07-20", "Pendiente"));

        tablaReservas.setItems(listaReservas);
    }

    @FXML
    private void verFactura() {
        ReservaDTO reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada != null) {
            lblInfo.setText("Mostrando factura para: " + reservaSeleccionada.getNombreAlojamiento());
            // Aquí va la lógica para mostrar la factura
        } else {
            lblInfo.setText("Seleccione una reserva primero.");
        }
    }

    public static class ReservaDTO {
        private final String nombreAlojamiento;
        private final String fechaInicio;
        private final String fechaFin;
        private final String estado;

        public ReservaDTO(String nombreAlojamiento, String fechaInicio, String fechaFin, String estado) {
            this.nombreAlojamiento = nombreAlojamiento;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.estado = estado;
        }

        public String getNombreAlojamiento() {
            return nombreAlojamiento;
        }

        public String getFechaInicio() {
            return fechaInicio;
        }

        public String getFechaFin() {
            return fechaFin;
        }

        public String getEstado() {
            return estado;
        }
    }
}