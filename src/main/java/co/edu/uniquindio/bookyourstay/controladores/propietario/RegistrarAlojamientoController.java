package co.edu.uniquindio.bookyourstay.controladores.propietario;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.servicios.AlojamientoServicio;
import co.edu.uniquindio.bookyourstay.singleton.GestorAlojamientos;
import co.edu.uniquindio.bookyourstay.singleton.UsuarioActual;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RegistrarAlojamientoController {

    // Campos de texto
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecioNoche;
    @FXML private TextField txtCapacidadMax;
    @FXML private TextField txtCiudad;

    // Otros controles
    @FXML private ComboBox<String> cmbTipoAlojamiento;
    @FXML private Button btnCancelar;

    // Variables de control
    private int indiceEdicion = -1;

    @FXML
    private void registrarAlojamiento() {
        try {
            // Validar campos
            validarCampos();

            // Obtener valores
            String nombre = txtNombre.getText();
            String ciudad = txtCiudad.getText();
            String descripcion = txtDescripcion.getText();
            int capacidadMax = Integer.parseInt(txtCapacidadMax.getText());
            float precioNoche = Float.parseFloat(txtPrecioNoche.getText());
            TipoAlojamiento tipo = TipoAlojamiento.valueOf(cmbTipoAlojamiento.getValue().toUpperCase());

            if (indiceEdicion >= 0) {
                // Modo edición
                actualizarAlojamientoExistente(nombre, ciudad, descripcion, capacidadMax, precioNoche, tipo);
                mostrarAlerta("Alojamiento actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                // Modo creación
                crearNuevoAlojamiento(nombre, ciudad, descripcion, capacidadMax, precioNoche, tipo);
                mostrarAlerta("Alojamiento registrado correctamente", Alert.AlertType.INFORMATION);
            }

            limpiarCampos();
            volverAInicio();

        } catch (NumberFormatException e) {
            mostrarAlerta("Los campos numéricos deben contener valores válidos", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void validarCampos() {
        if (txtNombre.getText().isEmpty() || txtCiudad.getText().isEmpty() || txtDescripcion.getText().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos deben estar llenos");
        }
        if (Integer.parseInt(txtCapacidadMax.getText()) <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser positiva");
        }
        if (Float.parseFloat(txtPrecioNoche.getText()) <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser positivo");
        }
    }

    private void actualizarAlojamientoExistente(String nombre, String ciudad, String descripcion,
                                                int capacidadMax, float precioNoche, TipoAlojamiento tipo) {
        Alojamiento alojamiento = GestorAlojamientos.getInstancia().getAlojamientos().get(indiceEdicion);
        alojamiento.setNombre(nombre);
        alojamiento.setCiudad(ciudad);
        alojamiento.setDescripcion(descripcion);
        alojamiento.setCapacidadMax(capacidadMax);
        alojamiento.setPrecioNoche(precioNoche);
        alojamiento.setTipo(tipo);
    }

    private void crearNuevoAlojamiento(String nombre, String ciudad, String descripcion,
                                       int capacidadMax, float precioNoche, TipoAlojamiento tipo) {
        try {
            // 1. Crear instancia específica
            Alojamiento alojamiento = switch (tipo) {
                case CASA -> new Casa(nombre, ciudad, descripcion, capacidadMax, precioNoche);
                case APARTAMENTO -> new Apartamento(nombre, ciudad, descripcion, capacidadMax, precioNoche);
                case HOTEL -> new Hotel(nombre, ciudad, descripcion, capacidadMax, precioNoche);
            };

            // 2. Añadir servicios básicos (opcional)
            alojamiento.setServicios(List.of("Wifi", "Toallas", "Aire acondicionado"));

            // 3. Guardar en el sistema
            GestorAlojamientos.getInstancia().agregarAlojamiento(alojamiento);
            AlojamientoServicio.obtenerInstancia()
                    .actualizarAlojamiento(alojamiento.getId(), alojamiento); // Usa el método existente

        } catch (Exception e) {
        }
    }

    public void cargarDatosAlojamiento(Alojamiento alojamiento, int indice) {
        this.indiceEdicion = indice;
        txtNombre.setText(alojamiento.getNombre());
        txtCiudad.setText(alojamiento.getCiudad());
        txtDescripcion.setText(alojamiento.getDescripcion());
        txtCapacidadMax.setText(String.valueOf(alojamiento.getCapacidadMax()));
        txtPrecioNoche.setText(String.valueOf(alojamiento.getPrecioNoche()));
        cmbTipoAlojamiento.setValue(alojamiento.getTipo().toString());
    }

    @FXML
    private void cancelar() {
        volverAInicio();
    }

    private void volverAInicio() {
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
        indiceEdicion = -1;
        txtNombre.clear();
        txtCiudad.clear();
        txtDescripcion.clear();
        txtPrecioNoche.clear();
        txtCapacidadMax.clear();
        cmbTipoAlojamiento.setValue(null);
    }
}