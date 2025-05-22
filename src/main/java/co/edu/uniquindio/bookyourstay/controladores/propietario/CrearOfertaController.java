package co.edu.uniquindio.bookyourstay.controladores.propietario;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class CrearOfertaController {

    @FXML
    private ComboBox<String> comboAlojamiento;

    @FXML
    private DatePicker fechaInicio;

    @FXML
    private DatePicker fechaFin;

    @FXML
    private TextField campoPrecio;

    @FXML
    private ComboBox<String> comboTipoOferta;

    @FXML
    private Button btnCrearOferta;

    @FXML
    private void initialize() {
        comboAlojamiento.getItems().addAll("Casa en Armenia", "Apartamento en Salento");
        comboTipoOferta.getItems().addAll("Oferta Regular", "Descuento Especial", "Temporada Alta");
    }

    @FXML
    private void crearOferta() {
        String alojamiento = comboAlojamiento.getValue();
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();
        String tipo = comboTipoOferta.getValue();
        String precio = campoPrecio.getText();

        // Validación y lógica de creación
        if (alojamiento != null && inicio != null && fin != null && tipo != null && !precio.isEmpty()) {
            // Lógica para guardar la oferta
            System.out.println("Oferta creada para " + alojamiento);
        } else {
            System.out.println("Faltan campos por completar.");
        }
    }
}