package co.edu.uniquindio.bookyourstay.controladores.cliente;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.servicios.AlojamientoServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class BuscarAlojamientosController {

    @FXML
    private TextField ciudadField;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private ComboBox<String> tipoAlojamientoCombo;

    @FXML
    private ListView<String> resultadosList;

    // Instancia del servicio
    private final AlojamientoServicio alojamientoServicio = AlojamientoServicio.obtenerInstancia();

    @FXML
    public void initialize() {
        tipoAlojamientoCombo.setItems(FXCollections.observableArrayList("Casa", "Apartamento", "Hotel"));
    }
    @FXML
    private void regresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/cliente/cliente_inicio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ciudadField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void buscar() {
        String ciudad = ciudadField.getText();
        if (ciudad.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar una ciudad");
            return;
        }

        try {
            ObservableList<Alojamiento> alojamientos = alojamientoServicio.buscarPorCiudad(ciudad);

            if (alojamientos.isEmpty()) {
                mostrarAlerta("Información", "No se encontraron alojamientos para esa ciudad");
                resultadosList.getItems().clear();
            } else {
                resultadosList.setItems(
                        FXCollections.observableArrayList(
                                alojamientos.stream()
                                        .map(Alojamiento::getNombre)
                                        .toList()
                        )
                );
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al buscar alojamientos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}