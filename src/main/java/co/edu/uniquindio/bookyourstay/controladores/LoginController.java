package co.edu.uniquindio.bookyourstay.controladores;

import co.edu.uniquindio.bookyourstay.modelo.enums.RolUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.servicios.UsuarioServicio;

import java.io.IOException;

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

        try {
            Usuario usuario = usuarioServicio.autenticarUsuario(correo, contrasena);

            if (usuario == null) {
                mensajeLabel.setText("Credenciales incorrectas.");
                return;
            }

            RolUsuario rol = usuario.getRol();

            if (rol == null) {
                mensajeLabel.setText("El usuario no tiene rol asignado.");
                return;
            }

            // Limpiamos mensaje antes de cargar la vista
            mensajeLabel.setText("");

            switch (rol) {
                case CLIENTE -> cargarVista("/co/edu/uniquindio/bookyourstay/vistas/cliente/cliente_inicio.fxml", "Inicio Cliente");
                case PROPIETARIO -> cargarVista("/co/edu/uniquindio/bookyourstay/vistas/propietario/propietario_inicio.fxml", "Inicio Propietario");
                case ADMINISTRADOR -> cargarVista("/co/edu/uniquindio/bookyourstay/vistas/administrador/admin_inicio.fxml", "Inicio Administrador");
                default -> mensajeLabel.setText("Rol no reconocido.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mensajeLabel.setText("Error al iniciar sesión.");
        }
    }

    private void cargarVista(String rutaFXML, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Scene scene = new Scene(loader.load());

            // Obtengo el Stage actual desde cualquier nodo (en este caso correoField)
            Stage stage = (Stage) correoField.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle(tituloVentana);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mensajeLabel.setText("No se pudo cargar la vista.");
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
    @FXML
    private void irAMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bookyourstay/vistas/menu_principal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) correoField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mensajeLabel.setText("No se pudo abrir el menú principal.");
        }
    }
}