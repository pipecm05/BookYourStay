package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

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

    @FXML
    public void initialize() {
        tipoAlojamientoCombo.setItems(FXCollections.observableArrayList("Casa", "Apartamento", "Hotel"));
    }

    @FXML
    private void buscar() {
        String ciudad = ciudadField.getText();
        if(ciudad.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar una ciudad");
            return;
        }

        // Simulación de búsqueda
        resultadosList.setItems(FXCollections.observableArrayList(
                "Casa en " + ciudad,
                "Apartamento en " + ciudad,
                "Hotel en " + ciudad
        ));
    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}