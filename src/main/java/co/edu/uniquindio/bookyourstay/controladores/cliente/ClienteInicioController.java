package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class ClienteInicioController {

    @FXML
    private void buscarAlojamientos() {
        System.out.println("Navegando a Buscar Alojamientos");
        // TODO: cargar buscar_alojamientos.fxml
    }

    @FXML
    private void verMisReservas() {
        System.out.println("Navegando a Mis Reservas");
        // TODO: cargar mis_reservas.fxml
    }

    @FXML
    private void crearReseña() {
        System.out.println("Navegando a Crear Reseña");
        // TODO: cargar crear_reseña.fxml
    }

    @FXML
    private void verFactura() {
        System.out.println("Navegando a Factura");
        // TODO: cargar factura.fxml
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}