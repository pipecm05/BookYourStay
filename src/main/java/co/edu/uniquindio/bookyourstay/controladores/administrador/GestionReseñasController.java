package co.edu.uniquindio.bookyourstay.controladores.administrador;

import co.edu.uniquindio.bookyourstay.modelo.Reseña;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GestionReseñasController {

    @FXML
    private TableView<Reseña> tablaReseñas;

    @FXML
    private TableColumn<Reseña, String> columnaCliente;

    @FXML
    private TableColumn<Reseña, String> columnaAlojamiento;

    @FXML
    private TableColumn<Reseña, String> columnaComentario;

    @FXML
    private TableColumn<Reseña, Integer> columnaCalificacion;

    @FXML
    private Button btnEliminar;

    private ObservableList<Reseña> listaReseñas;

    @FXML
    public void initialize() {
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        columnaAlojamiento.setCellValueFactory(new PropertyValueFactory<>("nombreAlojamiento"));
        columnaComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        columnaCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        cargarReseñas();
    }

    private void cargarReseñas() {
        // Aquí iría la carga real desde la base de datos o lista del sistema
        listaReseñas = FXCollections.observableArrayList(
                new Reseña("Juan Perez", "Casa Bonita", "Muy buen lugar", 5),
                new Reseña("Ana Gómez", "Apartamento Central", "Limpio y cómodo", 4),
                new Reseña("Carlos Ruiz", "Hotel Sol", "Servicio regular", 3)
        );
        tablaReseñas.setItems(listaReseñas);
    }

    @FXML
    private void eliminarReseña() {
        Reseña seleccion = tablaReseñas.getSelectionModel().getSelectedItem();
        if(seleccion != null) {
            listaReseñas.remove(seleccion);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Eliminar reseña");
            alert.setHeaderText(null);
            alert.setContentText("Reseña eliminada correctamente.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Eliminar reseña");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione una reseña para eliminar.");
            alert.showAndWait();
        }
    }
}