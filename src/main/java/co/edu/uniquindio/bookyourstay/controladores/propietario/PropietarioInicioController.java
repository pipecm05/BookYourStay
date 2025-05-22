package co.edu.uniquindio.bookyourstay.controladores.propietario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.event.ActionEvent;

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
    private void registrarAlojamiento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/propietario/registrar_alojamiento.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registrar Alojamiento");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verMisAlojamientos(ActionEvent event) {
        // Aquí irá el cambio de escena para mis alojamientos
    }

    @FXML
    private void crearOferta(ActionEvent event) {
        // Aquí irá el cambio de escena para crear oferta
    }

    @FXML
    private void verReservas(ActionEvent event) {
        // Aquí irá el cambio de escena para ver reservas
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/menu_principal.fxml"));
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