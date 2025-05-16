package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.servicio.IUsuarioServicio;
import co.edu.uniquindio.bookyourstay.servicio.UsuarioServicio;
import co.edu.uniquindio.bookyourstay.utilidades.Alerta;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControlador {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnRegresar;
    @FXML private Button btnRecuperarPassword;

    private final IUsuarioServicio usuarioServicio = UsuarioServicio.getInstancia();

    @FXML
    private void initialize() {
        // Configuración inicial si es necesaria
    }

    @FXML
    private void iniciarSesion() {
        try {
            String email = txtEmail.getText();
            String password = txtPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                Alerta.mostrarAlerta("Error", "Todos los campos son obligatorios", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }

            usuarioServicio.iniciarSesion(email, password);
            navegarAPanelPrincipal();

        } catch (Exception e) {
            Alerta.mostrarAlerta("Error", e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void solicitarRecuperacionPassword() {
        try {
            String email = txtEmail.getText();

            if (email.isEmpty()) {
                Alerta.mostrarAlerta("Error", "Ingrese su email para recuperar la contraseña", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }

            usuarioServicio.solicitarCambioPassword(email);
            Alerta.mostrarAlerta("Éxito", "Se ha enviado un código a su email para cambiar la contraseña", javafx.scene.control.Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            Alerta.mostrarAlerta("Error", e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    private void navegarAPanelPrincipal() {
        // Lógica para navegar al panel principal según el tipo de usuario
    }
}