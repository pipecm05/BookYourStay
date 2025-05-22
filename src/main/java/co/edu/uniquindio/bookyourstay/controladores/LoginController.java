package co.edu.uniquindio.bookyourstay.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.servicios.UsuarioServicio;

public class LoginController {

    @FXML
    private TextField correoField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private Label mensajeLabel;

    // Usar el método estático para obtener la instancia
    private final UsuarioServicio usuarioServicio = UsuarioServicio.obtenerInstancia();

    @FXML
    private void iniciarSesion() {
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();

        Usuario usuario = null;
        try {
            usuario = usuarioServicio.iniciarSesion(correo, contrasena);
        } catch (Exception e) {
            mensajeLabel.setText(e.getMessage());
            return;
        }

        if (usuario == null) {
            mensajeLabel.setText("Correo o contraseña incorrectos");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            switch (usuario.getRol()) {
                case CLIENTE:
                    loader.setLocation(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/cliente_inicio.fxml"));
                    break;
                case PROPIETARIO:
                    loader.setLocation(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/propietaraio_inicio.fxml"));
                    break;
                case ADMINISTRADOR:
                    loader.setLocation(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/admin_inicio.fxml"));
                    break;
                default:
                    mensajeLabel.setText("Rol no reconocido");
                    return;
            }

            Parent root = loader.load();
            Stage stage = (Stage) correoField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeLabel.setText("Error al cargar la interfaz");
        }
    }

    @FXML
    private void irARegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registro.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) correoField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeLabel.setText("No se pudo abrir el registro.");
        }
    }
}