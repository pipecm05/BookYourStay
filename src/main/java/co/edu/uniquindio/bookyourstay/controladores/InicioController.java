package co.edu.uniquindio.bookyourstay.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InicioController {

    @FXML
    private Label usuarioLabel;

    private String tipoUsuario = "Cliente"; // Simulado para ejemplo. En producción, cargar el tipo real

    @FXML
    public void initialize() {
        // Simular mostrar nombre de usuario
        usuarioLabel.setText("Usuario: Juan Pérez");
    }

    @FXML
    private void irMenuPrincipal() {
        System.out.println("Redirigiendo según tipo de usuario: " + tipoUsuario);
        // TODO: Implementar navegación real según tipoUsuario
        // Ejemplo: si es Cliente, cargar cliente_inicio.fxml
    }
}