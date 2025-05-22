package co.edu.uniquindio.bookyourstay.controladores.administrador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AdminInicioController {

    @FXML
    private Button btnGestionUsuarios;

    @FXML
    private Button btnEstadisticas;

    @FXML
    private Button btnGestionReseñas;

    @FXML
    private void inicializar() {
        // Inicialización si se requiere
    }

    @FXML
    private void gestionarUsuarios(ActionEvent event) {
        // Lógica para abrir la ventana de gestión de usuarios
        System.out.println("Abrir gestión de usuarios");
    }

    @FXML
    private void verEstadisticas(ActionEvent event) {
        // Lógica para abrir la ventana de estadísticas
        System.out.println("Abrir estadísticas");
    }

    @FXML
    private void gestionarReseñas(ActionEvent event) {
        // Lógica para abrir la ventana de gestión de reseñas
        System.out.println("Abrir gestión de reseñas");
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