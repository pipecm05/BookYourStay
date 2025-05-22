package co.edu.uniquindio.bookyourstay.controladores.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class DetalleOfertaController {

    @FXML
    private Label lblTitulo, lblDescripcion, lblPrecio, lblDisponibilidad, lblTipoAlojamiento;

    @FXML
    private ImageView imagenAlojamiento;

    @FXML
    private Button btnReservar, btnVolver;

    @FXML
    private void reservarOferta() {
        // Lógica para procesar la reserva
    }

    @FXML
    private void volver() {
        // Lógica para volver a la vista anterior
    }

    public void inicializarDatos(String titulo, String descripcion, double precio, boolean disponible, String tipo) {
        lblTitulo.setText(titulo);
        lblDescripcion.setText(descripcion);
        lblPrecio.setText("$" + precio);
        lblDisponibilidad.setText(disponible ? "Disponible" : "No disponible");
        lblTipoAlojamiento.setText(tipo);
    }
}