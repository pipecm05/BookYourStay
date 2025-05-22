package co.edu.uniquindio.bookyourstay.controladores.propietario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MisAlojamientosController {

    @FXML
    private ListView<String> listaAlojamientos;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private void initialize() {
        ObservableList<String> alojamientos = FXCollections.observableArrayList(
                "Casa en Armenia", "Apartamento en Salento", "Hotel en Quimbaya"
        );
        listaAlojamientos.setItems(alojamientos);
    }

    @FXML
    private void editarAlojamiento() {
        // Lógica para editar alojamiento seleccionado
    }

    @FXML
    private void eliminarAlojamiento() {
        // Lógica para eliminar alojamiento seleccionado
    }
}