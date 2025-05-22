package co.edu.uniquindio.bookyourstay.controladores.propietario;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RegistrarAlojamientoController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtCapacidad;

    @FXML
    private ComboBox<String> cmbTipoAlojamiento;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private void registrarAlojamiento() {
        // Lógica para registrar alojamiento usando el patrón Factory
    }

    @FXML
    private void cancelar() {
        // Lógica para volver a la pantalla anterior
    }

    @FXML
    public void initialize() {
        cmbTipoAlojamiento.getItems().addAll("Casa", "Apartamento", "Hotel");
    }
}