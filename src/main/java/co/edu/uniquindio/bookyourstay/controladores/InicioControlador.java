package co.edu.uniquindio.bookyourstay.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioControlador {
    @FXML private VBox root;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnRegistrarse;
    @FXML private Button btnVerAlojamientos;

    @FXML
    private void initialize() {
        // Configuración inicial si es necesaria
    }

    @FXML
    private void navegarALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navegarARegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/registro.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegistrarse.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro de Usuario");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verAlojamientosPublicos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/alojamientosPublicos.fxml"));
            Parent root = loader.load();

            AlojamientosPublicosControlador controlador = loader.getController();
            controlador.cargarAlojamientos();

            Stage stage = (Stage) btnVerAlojamientos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Alojamientos Disponibles");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}