package co.edu.uniquindio.bookyourstay.controladores.propietario;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.singleton.GestorAlojamientos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class MisAlojamientosController {

    @FXML
    private ListView<String> listaAlojamientos;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private void initialize() {
        cargarAlojamientos();
    }

    private void cargarAlojamientos() {
        ObservableList<String> nombresAlojamientos = FXCollections.observableArrayList();
        for (Alojamiento alojamiento : GestorAlojamientos.getInstancia().getAlojamientos()) {
            nombresAlojamientos.add(alojamiento.getNombre() + " - " + alojamiento.getCiudad() +
                    " (" + alojamiento.getTipo() + ")");
        }
        listaAlojamientos.setItems(nombresAlojamientos);
    }
    @FXML
    private void editarAlojamiento() {
        int indiceSeleccionado = listaAlojamientos.getSelectionModel().getSelectedIndex();

        if (indiceSeleccionado == -1) {
            mostrarAlerta("Por favor selecciona un alojamiento para editar", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Obtener el alojamiento seleccionado
            Alojamiento alojamiento = GestorAlojamientos.getInstancia().getAlojamientos().get(indiceSeleccionado);

            // Cargar la vista de edición (podría ser la misma que para registrar)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/propietario/registrar_alojamiento.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtener el controlador y pasar los datos del alojamiento
            RegistrarAlojamientoController controller = loader.getController();
            controller.cargarDatosAlojamiento(alojamiento, indiceSeleccionado);

            Stage stage = (Stage) btnEditar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Editar Alojamiento");
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error al cargar la vista de edición", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarAlojamiento() {
        int indiceSeleccionado = listaAlojamientos.getSelectionModel().getSelectedIndex();

        if (indiceSeleccionado == -1) {
            mostrarAlerta("Por favor selecciona un alojamiento para eliminar", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que quieres eliminar este alojamiento?");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            // Eliminar el alojamiento
            GestorAlojamientos.getInstancia().getAlojamientos().remove(indiceSeleccionado);
            cargarAlojamientos(); // Refrescar la lista
            mostrarAlerta("Alojamiento eliminado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
