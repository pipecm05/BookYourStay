package co.edu.uniquindio.bookyourstay.controladores.administrador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class GestionUsuariosController {

    @FXML private TableView<UsuarioTableModel> tablaUsuarios;
    @FXML private TableColumn<UsuarioTableModel, String> colNombre;
    @FXML private TableColumn<UsuarioTableModel, String> colCorreo;
    @FXML private TableColumn<UsuarioTableModel, String> colRol;
    @FXML private TableColumn<UsuarioTableModel, Boolean> colActivo;
    @FXML private Button btnSuspender;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private TextField txtBusqueda;

    private ObservableList<UsuarioTableModel> listaUsuarios;
    private ObservableList<UsuarioTableModel> listaFiltrada;

    @FXML
    private void initialize() {
        configurarColumnas();
        cargarUsuarios();
        configurarEventos();
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // Formato adicional para columnas
        colNombre.setStyle("-fx-alignment: CENTER-LEFT;");
        colCorreo.setStyle("-fx-alignment: CENTER-LEFT;");
        colRol.setStyle("-fx-alignment: CENTER;");
    }

    private void cargarUsuarios() {
        // Datos de ejemplo - en una aplicación real esto vendría de un servicio
        listaUsuarios = FXCollections.observableArrayList(
                new UsuarioTableModel("Juan Perez", "juan@ejemplo.com", "Cliente", true),
                new UsuarioTableModel("Maria Gomez", "maria@ejemplo.com", "Propietario", true),
                new UsuarioTableModel("Admin Sistema", "admin@bookyourstay.com", "Administrador", true)
        );

        listaFiltrada = FXCollections.observableArrayList(listaUsuarios);
        tablaUsuarios.setItems(listaFiltrada);
    }

    private void configurarEventos() {
        // Doble clic para editar
        tablaUsuarios.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                editarUsuario();
            }
        });

        // Filtrado de usuarios
        txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuarios(newValue);
        });
    }

    @FXML
    private void suspenderUsuario() {
        UsuarioTableModel usuario = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (usuario == null) {
            mostrarAlerta("Advertencia", "Seleccione un usuario para suspender", Alert.AlertType.WARNING);
            return;
        }

        String accion = usuario.getActivo() ? "suspender" : "reactivar";

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar acción");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(String.format("¿Está seguro que desea %s al usuario %s?", accion, usuario.getNombre()));

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                usuario.setActivo(!usuario.getActivo());
                tablaUsuarios.refresh();
                mostrarAlerta("Éxito", String.format("Usuario %s correctamente", usuario.getActivo() ? "reactivado" : "suspendido"),
                        Alert.AlertType.INFORMATION);
            }
        });
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
    @FXML
    private void eliminarUsuario() {
        UsuarioTableModel usuario = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (usuario == null) {
            mostrarAlerta("Advertencia", "Seleccione un usuario para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(String.format("¿Está seguro que desea eliminar permanentemente al usuario %s?", usuario.getNombre()));

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                listaUsuarios.remove(usuario);
                filtrarUsuarios(txtBusqueda.getText());
                mostrarAlerta("Éxito", "Usuario eliminado correctamente", Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void editarUsuario() {
        UsuarioTableModel usuario = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (usuario == null) {
            mostrarAlerta("Advertencia", "Seleccione un usuario para editar", Alert.AlertType.WARNING);
            return;
        }

        // Aquí iría la lógica para abrir un diálogo de edición
        mostrarAlerta("Información", "Funcionalidad de edición en desarrollo", Alert.AlertType.INFORMATION);
    }

    private void filtrarUsuarios(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            listaFiltrada.setAll(listaUsuarios);
            return;
        }

        listaFiltrada.clear();
        String lowerCaseFilter = filtro.toLowerCase();

        for (UsuarioTableModel usuario : listaUsuarios) {
            if (usuario.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                    usuario.getCorreo().toLowerCase().contains(lowerCaseFilter) ||
                    usuario.getRol().toLowerCase().contains(lowerCaseFilter) ||
                    (usuario.getActivo() ? "activo" : "inactivo").contains(lowerCaseFilter)) {
                listaFiltrada.add(usuario);
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    // Clase interna para representar datos de usuario en la tabla
    public static class UsuarioTableModel {
        private String nombre;
        private String correo;
        private String rol;
        private Boolean activo;

        public UsuarioTableModel(String nombre, String correo, String rol, Boolean activo) {
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
            this.activo = activo;
        }

        // Getters y setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }

        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
    }
}