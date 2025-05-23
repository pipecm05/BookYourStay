package co.edu.uniquindio.bookyourstay.controladores.administrador;

import co.edu.uniquindio.bookyourstay.modelo.Reseña;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

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
    @FXML
    private Button btnEditar;
    @FXML
    private TextField txtFiltro;

    private ObservableList<Reseña> listaReseñas;
    private ObservableList<Reseña> listaFiltrada;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarReseñas();
        configurarEventos();
    }

    private void configurarColumnas() {
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        columnaAlojamiento.setCellValueFactory(new PropertyValueFactory<>("nombreAlojamiento"));
        columnaComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        columnaCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        // Formato adicional para columnas
        columnaComentario.setStyle("-fx-alignment: CENTER-LEFT;");
        columnaCalificacion.setStyle("-fx-alignment: CENTER;");
    }

    private void cargarReseñas() {
        // Datos de ejemplo - en una aplicación real esto vendría de un servicio
        listaReseñas = FXCollections.observableArrayList(
                new Reseña("Juan Perez", "Casa Bonita", "Muy buen lugar, excelente atención", 5),
                new Reseña("Ana Gómez", "Apartamento Central", "Limpio y cómodo, buena ubicación", 4),
                new Reseña("Carlos Ruiz", "Hotel Sol", "Servicio regular, habitaciones pequeñas", 3),
                new Reseña("María López", "Cabaña Montaña", "Vistas espectaculares pero wifi lento", 4),
                new Reseña("Pedro Sánchez", "Loft Moderno", "Diseño increíble, falta algo de limpieza", 4)
        );

        listaFiltrada = FXCollections.observableArrayList(listaReseñas);
        tablaReseñas.setItems(listaFiltrada);
    }

    private void configurarEventos() {
        // Doble clic para editar
        tablaReseñas.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                editarReseña();
            }
        });

        // Filtrado de reseñas
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarReseñas(newValue);
        });
    }

    @FXML
    private void eliminarReseña(ActionEvent event) {
        Reseña seleccion = tablaReseñas.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarAlerta("Advertencia", "Seleccione una reseña para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro que desea eliminar esta reseña?\nEsta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            listaReseñas.remove(seleccion);
            filtrarReseñas(txtFiltro.getText());
            mostrarAlerta("Éxito", "Reseña eliminada correctamente", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void editarReseña() {
        Reseña seleccion = tablaReseñas.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarAlerta("Advertencia", "Seleccione una reseña para editar", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Cargar el diálogo de edición
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/administrador/editar_reseña.fxml"));
            Parent root = loader.load();


            // Mostrar el diálogo
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Editar Reseña");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refrescar la tabla si se realizaron cambios
            tablaReseñas.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la ventana de edición", Alert.AlertType.ERROR);
        }
    }

    private void filtrarReseñas(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            listaFiltrada.setAll(listaReseñas);
            return;
        }

        listaFiltrada.clear();
        String lowerCaseFilter = filtro.toLowerCase();

        for (Reseña reseña : listaReseñas) {
            if (reseña.getNombreCliente().toLowerCase().contains(lowerCaseFilter) ||
                    reseña.getNombreAlojamiento().toLowerCase().contains(lowerCaseFilter) ||
                    reseña.getComentario().toLowerCase().contains(lowerCaseFilter) ||
                    String.valueOf(reseña.getCalificacion()).contains(lowerCaseFilter)) {
                listaFiltrada.add(reseña);
            }
        }
    }

    @FXML
    private void volverAInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/administrador/admin_inicio.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista de inicio", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}