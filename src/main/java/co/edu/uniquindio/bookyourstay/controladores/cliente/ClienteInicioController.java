package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ClienteInicioController {

    @FXML
    private void buscarAlojamientos(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/cliente/buscar_alojamiento.fxml", "Buscar Alojamientos", event);
    }

    @FXML
    private void verMisReservas(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/cliente/mis_reservas.fxml", "Mis Reservas", event);
    }

    @FXML
    private void crearReseña(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/cliente/crear_reseña.fxml", "Crear Reseña", event);
    }

    @FXML
    private void verFactura(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/cliente/factura.fxml", "Factura", event);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/bookyourstay/vistas/menu_principal.fxml", "Menú Principal", event);
    }

    private void cargarVista(String rutaFXML, String titulo, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista: " + titulo);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}