package co.edu.uniquindio.bookyourstay.controladores.propietario;

import co.edu.uniquindio.bookyourstay.modelo.Apartamento;
import co.edu.uniquindio.bookyourstay.modelo.Casa;
import co.edu.uniquindio.bookyourstay.modelo.Hotel;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrarAlojamientoController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtPrecioNoche;

    @FXML
    private TextField txtCapacidadMax;
    @FXML
    private TextField txtCiudad;

    @FXML
    private ComboBox<String> cmbTipoAlojamiento;

    @FXML
    private Button btnCancelar;

    @FXML
    private void registrarAlojamiento() {
        try {
            // Obtener valores
            String nombre = txtNombre.getText();
            String ciudad = txtCiudad.getText();
            String descripcion = txtDescripcion.getText();
            int capacidadMax = Integer.parseInt(txtCapacidadMax.getText());
            float precioNoche = Float.parseFloat(txtPrecioNoche.getText());
            TipoAlojamiento tipo = TipoAlojamiento.valueOf(cmbTipoAlojamiento.getValue().toUpperCase());

            // Validaciones
            if (nombre.isEmpty() || ciudad.isEmpty() || descripcion.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar llenos");
            }
            if (capacidadMax <= 0) {
                throw new IllegalArgumentException("La capacidad máxima debe ser positiva");
            }
            if (precioNoche <= 0) {
                throw new IllegalArgumentException("El precio por noche debe ser positivo");
            }

            // Crear alojamiento
            Alojamiento alojamiento = switch (tipo) {
                case CASA -> new Casa();
                case APARTAMENTO -> new Apartamento();
                case HOTEL -> new Hotel();
            };

            // Configurar propiedades
            alojamiento.setNombre(nombre);
            alojamiento.setCiudad(ciudad);
            alojamiento.setDescripcion(descripcion);
            alojamiento.setCapacidadMax(capacidadMax);
            alojamiento.setPrecioNoche(precioNoche);
            alojamiento.setTipo(tipo);

            // Mostrar éxito
            mostrarAlerta("Alojamiento registrado:\n" + alojamiento, Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Los campos numéricos deben contener valores válidos", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancelar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/propietario/propietario_inicio.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inicio Propietario");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error al volver a la pantalla anterior.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void initialize() {
        cmbTipoAlojamiento.getItems().addAll("Casa", "Apartamento", "Hotel");
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtCiudad.clear();
        txtDescripcion.clear();
        txtPrecioNoche.clear();
        txtCapacidadMax.clear();
        cmbTipoAlojamiento.setValue(null);
    }
}