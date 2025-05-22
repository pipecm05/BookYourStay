package co.edu.uniquindio.bookyourstay.controladores.administrador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionUsuariosController {

    @FXML
    private TableView<UsuarioTableModel> tablaUsuarios;

    @FXML
    private TableColumn<UsuarioTableModel, String> colNombre;

    @FXML
    private TableColumn<UsuarioTableModel, String> colCorreo;

    @FXML
    private TableColumn<UsuarioTableModel, String> colRol;

    @FXML
    private TableColumn<UsuarioTableModel, Boolean> colActivo;

    @FXML
    private Button btnSuspender;

    @FXML
    private Button btnEliminar;

    private ObservableList<UsuarioTableModel> listaUsuarios;

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        // Aquí deberías cargar los usuarios desde tu servicio o repositorio
        listaUsuarios = FXCollections.observableArrayList(
                new UsuarioTableModel("Juan Perez", "juan@ejemplo.com", "Cliente", true),
                new UsuarioTableModel("Maria Gomez", "maria@ejemplo.com", "Propietario", true)
        );

        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void suspenderUsuario() {
        UsuarioTableModel usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            usuario.setActivo(false);
            tablaUsuarios.refresh();
        } else {
            mostrarAlerta("Por favor seleccione un usuario para suspender.");
        }
    }

    @FXML
    private void eliminarUsuario() {
        UsuarioTableModel usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            listaUsuarios.remove(usuario);
        } else {
            mostrarAlerta("Por favor seleccione un usuario para eliminar.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
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