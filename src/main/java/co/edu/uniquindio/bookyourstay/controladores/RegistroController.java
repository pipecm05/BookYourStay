package co.edu.uniquindio.bookyourstay.controladores;

import co.edu.uniquindio.bookyourstay.modelo.enums.RolUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.servicios.UsuarioServicio;

public class RegistroController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField cedulaField;

    @FXML
    private TextField correoField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private PasswordField confirmarContrasenaField;

    @FXML
    private ComboBox<RolUsuario> rolUsuarioCombo;

    @FXML
    private Label mensajeLabel;

    private final UsuarioServicio usuarioServicio = UsuarioServicio.obtenerInstancia();

    @FXML
    public void initialize() {
        if (rolUsuarioCombo != null) {
            rolUsuarioCombo.getItems().setAll(RolUsuario.values());
        } else {
            System.err.println("Error: tipoUsuarioCombo es null en initialize()");
        }
    }

    @FXML
    private void registrarUsuario() {
        String nombre = nombreField.getText();
        String cedula = cedulaField.getText();
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();
        String confirmar = confirmarContrasenaField.getText();
        RolUsuario rolUsuario = rolUsuarioCombo.getValue();

        if (nombre.isBlank() || cedula.isBlank() || correo.isBlank() || contrasena.isBlank() || confirmar.isBlank() || rolUsuario == null) {
            mensajeLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!contrasena.equals(confirmar)) {
            mensajeLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        try {
            Usuario usuarioRegistrado = usuarioServicio.registrarUsuario(
                    new Usuario(nombre, cedula, correo, contrasena, rolUsuario)
            );
            mensajeLabel.setText("Usuario registrado exitosamente.");
            irALogin();
        } catch (IllegalArgumentException e) {
            mensajeLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void irALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeLabel.setText("No se pudo cargar la vista de inicio de sesión.");
        }
    }
}