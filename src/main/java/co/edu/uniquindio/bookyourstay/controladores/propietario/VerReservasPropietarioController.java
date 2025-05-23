package co.edu.uniquindio.bookyourstay.controladores.propietario;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import co.edu.uniquindio.bookyourstay.singleton.GestorReservas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class VerReservasPropietarioController {

    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, String> colAlojamiento;
    @FXML private TableColumn<Reserva, String> colCiudad;
    @FXML private TableColumn<Reserva, String> colCliente;
    @FXML private TableColumn<Reserva, String> colFechaInicio;
    @FXML private TableColumn<Reserva, String> colFechaFin;
    @FXML private TableColumn<Reserva, String> colEstado;
    @FXML private ComboBox<String> comboFiltrarCiudad;
    @FXML private Button btnActualizar;
    @FXML private Button btnCancelarReserva;
    @FXML private Button btnVolver;

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarFiltros();
        cargarReservas();
        configurarEventos();
    }

    private void configurarColumnas() {
        // Configuración para la columna de Alojamiento
        colAlojamiento.setCellValueFactory(cellData -> {
            Alojamiento a = cellData.getValue().getAlojamiento();
            return a != null ? a.nombreProperty() : new SimpleStringProperty("No asignado");
        });

        // Configuración para la columna de Ciudad
        colCiudad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCiudadAlojamiento()));

        // Configuración para la columna de Cliente
        colCliente.setCellValueFactory(cellData -> {
            Cliente c = cellData.getValue().getCliente();
            return c != null ? new SimpleStringProperty(c.getNombre()) : new SimpleStringProperty("Sin cliente");
        });

        // Configuración para las columnas de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        colFechaInicio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaInicio().format(formatter)));

        colFechaFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaFin().format(formatter)));

        // Configuración para la columna de Estado
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    }

    private void configurarFiltros() {
        Set<String> ciudades = GestorReservas.getInstancia()
                .obtenerReservasPorPropietario(UsuarioActual.getInstancia().getId())
                .stream()
                .map(Reserva::getCiudadAlojamiento)
                .collect(Collectors.toSet());

        comboFiltrarCiudad.getItems().add("Todas");
        comboFiltrarCiudad.getItems().addAll(ciudades);
        comboFiltrarCiudad.getSelectionModel().selectFirst();

        comboFiltrarCiudad.valueProperty().addListener((obs, oldVal, newVal) -> {
            filtrarPorCiudad(newVal);
        });
    }

    private void cargarReservas() {
        ObservableList<Reserva> reservas = GestorReservas.getInstancia()
                .obtenerReservasPorPropietario(UsuarioActual.getInstancia().getId());
        tablaReservas.setItems(reservas);
    }

    private void filtrarPorCiudad(String ciudad) {
        if (ciudad == null || "Todas".equals(ciudad)) {
            cargarReservas();
        } else {
            ObservableList<Reserva> filtradas = tablaReservas.getItems()
                    .filtered(r -> r.getCiudadAlojamiento().equals(ciudad));
            tablaReservas.setItems(filtradas);
        }
    }

    private void configurarEventos() {
        tablaReservas.setRowFactory(tv -> {
            TableRow<Reserva> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    mostrarDetalleReserva(row.getItem());
                }
            });
            return row;
        });
    }

    // Resto de tus métodos (actualizarReservas, cancelarReserva, volver, etc.)
    @FXML
    private void actualizarReservas() {
        cargarReservas();
        mostrarAlerta("Lista de reservas actualizada", AlertType.INFORMATION);
    }

    @FXML
    private void cancelarReserva() {
        Reserva seleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Seleccione una reserva para cancelar", AlertType.WARNING);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Motivo de cancelación");
        dialog.setHeaderText("Por favor ingrese el motivo de cancelación");
        dialog.setContentText("Motivo:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent() && !resultado.get().isEmpty()) {
            try {
                GestorReservas.getInstancia().cancelarReserva(seleccionada, resultado.get());
                cargarReservas();
                mostrarAlerta("Reserva cancelada exitosamente", AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error al cancelar reserva: " + e.getMessage(), AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Debe ingresar un motivo para cancelar la reserva", AlertType.WARNING);
        }
    }

    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/co/edu/uniquindio/bookyourstay/vistas/propietario/propietario_inicio.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Inicio Propietario");
        } catch (IOException e) {
            mostrarAlerta("Error al cargar la vista principal", AlertType.ERROR);
        }
    }

    private void mostrarDetalleReserva(Reserva reserva) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Detalle de Reserva");
        alert.setHeaderText("Información completa de la reserva");
        alert.setContentText(reserva.generarResumen());
        alert.showAndWait();
    }

    private void mostrarAlerta(String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensaje del Sistema");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}