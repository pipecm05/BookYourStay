package co.edu.uniquindio.bookyourstay.controladores.propietario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class PropietarioInicioController {

    @FXML
    private Button btnRegistrarAlojamiento;

    @FXML
    private Button btnMisAlojamientos;

    @FXML
    private Button btnCrearOferta;

    @FXML
    private Button btnVerReservas;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private void registrarAlojamiento() {
        // Lógica para ir a la vista de registrar alojamiento
    }

    @FXML
    private void verMisAlojamientos() {
        // Lógica para ir a la vista de alojamientos del propietario
    }

    @FXML
    private void crearOferta() {
        // Lógica para ir a la vista de crear oferta
    }

    @FXML
    private void verReservas() {
        // Lógica para ir a la vista de ver reservas
    }

    @FXML
    private void cerrarSesion() {
        // Lógica para cerrar sesión y volver a inicio
    }
    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/comun/menu_principal.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}