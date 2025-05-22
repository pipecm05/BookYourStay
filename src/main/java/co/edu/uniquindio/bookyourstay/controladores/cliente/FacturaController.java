package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

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
}