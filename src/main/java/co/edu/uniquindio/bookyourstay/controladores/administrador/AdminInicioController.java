package co.edu.uniquindio.bookyourstay.controladores.administrador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminInicioController {

    @FXML
    private void gestionarUsuarios(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/administrador/gestion_usuarios.fxml", event);
    }

    @FXML
    private void verEstadisticas(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/administrador/estadisticas.fxml", event);
    }

    @FXML
    private void gestionarReseñas(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/administrador/gestion_reseñas.fxml", event);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/login.fxml", event);
    }

    /**
     * Carga la vista especificada en la misma ventana actual.
     *
     * @param rutaVista Ruta del archivo FXML
     * @param event     Evento de acción que disparó el cambio de vista
     */
    private void cargarVista(String rutaVista, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaVista));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí puedes mostrar una alerta al usuario si lo deseas
        }
    }
}