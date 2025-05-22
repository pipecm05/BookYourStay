package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;

public class FacturaController {

    @FXML
    private Label lblNombreCliente;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblCodigoReserva;

    @FXML
    private Label lblTotal;

    @FXML
    private TextArea txtDetalleFactura;

    public void cargarFactura(String nombreCliente, String fecha, String codigoReserva, double total, String detalles) {
        lblNombreCliente.setText(nombreCliente);
        lblFecha.setText(fecha);
        lblCodigoReserva.setText(codigoReserva);
        lblTotal.setText(String.format("$ %.2f", total));
        txtDetalleFactura.setText(detalles);
    }
    @FXML
    private Button btnVolver;  // o cualquier nodo que tengas en el FXML y que puedas usar para obtener la ventana

    @FXML
    private void volverAInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/cliente/cliente_inicio.fxml"));
            Parent root = loader.load();

            // Obtén la ventana actual usando el botón (o cualquier otro nodo)
            Stage stage = (Stage) btnVolver.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Inicio Cliente");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Opcional: mostrar mensaje de error en algún Label
        }
    }
}