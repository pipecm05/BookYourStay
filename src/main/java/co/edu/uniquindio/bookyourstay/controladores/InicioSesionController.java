package co.edu.uniquindio.bookyourstay.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.servicios.UsuarioServicio;

public class InicioSesionController extends BaseController {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnRegistrarse;

    private final UsuarioServicio usuarioServicio = UsuarioServicio.obtenerInstancia();

    @FXML
    private void initialize() {
        btnIniciarSesion.setOnAction(event -> handleIniciarSesion());
        btnRegistrarse.setOnAction(event -> handleRegistrarse());
    }

    private void handleIniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos");
            return;
        }

        try {
            Usuario usuario = usuarioServicio.iniciarSesion(email, password);
            mainApp.mostrarVistaPrincipal(usuario);
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void handleRegistrarse() {
        mainApp.mostrarRegistroCliente();
    }
}
